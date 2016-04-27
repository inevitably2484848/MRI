package edu.auburn.cardiomri.gui;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferShort;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.DisplayShutter;
import com.pixelmed.dicom.ModalityTransform;
import com.pixelmed.dicom.Overlay;
import com.pixelmed.dicom.RealWorldValueTransform;
import com.pixelmed.dicom.SUVTransform;
import com.pixelmed.dicom.VOITransform;
import com.pixelmed.display.SourceImage;

import edu.auburn.cardiomri.datastructure.DICOMImage;

/**
 * This was a fix to implement pixelMed, and most of this is pixelMed code.
 * This class converts a DICOM image into a construct image so that an image
 * may properly be operated on.
 * 
 *
 */
public class ConstructImage extends SourceImage {

    /***/
    @SuppressWarnings("unused")
    private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/display/SourceImage.java,v 1.81 2014/06/22 12:25:18 dclunie Exp $";

    /***/
    BufferedImage[] imgs;
    /***/
    int width;
    /***/
    int height;
    /***/
    int nframes;
    /***/
    double imgMin;
    /***/
    double imgMax;
    /***/
    double imgMean;
    /***/
    double imgStandardDeviation;
    /***/
    boolean signed;
    /***/
    boolean inverted;
    /***/
    int mask;
    /***/
    boolean isGrayscale;
    /***/
    boolean isPaletteColor;
    /***/
    boolean isYBR;
    /***/
    int pad;
    /***/
    int padRangeLimit;
    /***/
    boolean hasPad;
    /***/
    boolean useMaskedPadRange;
    /***/
    int useMaskedPadRangeStart;
    /***/
    int useMaskedPadRangeEnd;
    /***/
    boolean useNonMaskedSinglePadValue;
    /***/
    int nonMaskedSinglePadValue;
    /***/
    int backgroundValue;
    /***/
    String title;

    // stuff for (supplemental) palette color LUT
    /***/
    private int largestGray;
    /***/
    private int firstValueMapped;
    /***/
    private int numberOfEntries;
    /***/
    private int bitsPerEntry;
    /***/
    private short redTable[];
    /***/
    private short greenTable[];
    /***/
    private short blueTable[];

    /***/
    private SUVTransform suvTransform;

    /***/
    private RealWorldValueTransform realWorldValueTransform;

    /***/
    private ModalityTransform modalityTransform;

    /***/
    private VOITransform voiTransform;

    /***/
    private DisplayShutter displayShutter;

    /***/
    private Overlay overlay;

    /***/
    private ColorSpace srcColorSpace;
    /***/
    private ColorSpace dstColorSpace;

    protected BufferedImageSource bufferedImageSource = null;
    
    /**
     * Creates an instance of the image
     * 
     * @param image
     */
    public ConstructImage(DICOMImage image) {
        if (image != null) {
            try {
                constructSourceImage(image);
            } catch (DicomException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 
     * @param image
     * @throws DicomException
     */
    @SuppressWarnings("unused")
    void constructSourceImage(DICOMImage image) throws DicomException {

        width = getIntOrDefault(image.getColumns(), 0);
        height = getIntOrDefault(image.getRows(), 0);
        int depth = getIntOrDefault(image.getBitsAllocated(), 0);
        int samples = getIntOrDefault(image.getSamplesPerPixel(), 1);
        boolean byplane = (samples > 1 && getIntOrDefault(
                image.getPlanarConfiguration(), 0) == 1);
        nframes = getIntOrDefault(image.getNumberOfFrames(), 1);

        mask = 0;
        int extend = 0;
        int signbit = 1;
        int stored = getIntOrDefault(image.getBitsStored(), depth);

        if (depth < stored) {
            throw new DicomException("Unsupported Bits Allocated " + depth
                    + "\" less then Bits Stored " + stored);
        }
        {
            int s = stored;
            while (s-- > 0) {
                mask = mask << 1 | 1;
                signbit = signbit << 1;
            }
            signbit = signbit >> 1;
            extend = ~mask;
        }

        signed = getIntOrDefault(image.getPixelRepresentation(), 0) == 1;

        imgMin = signed ? 0x00007fff : 0x0000ffff;
        imgMax = signed ? 0xffff8000 : 0x00000000;

        pad = 0;
        hasPad = false;
        useMaskedPadRange = false;
        useNonMaskedSinglePadValue = false;

        Integer aPixelPaddingValue = image.getPixelPaddingValue();

        if (aPixelPaddingValue != null) {
            hasPad = true;
            pad = aPixelPaddingValue;
            padRangeLimit = getIntOrDefault(image.getPixelPaddingRangeLimit(),
                    0);

            useMaskedPadRangeStart = pad & mask;
            useMaskedPadRangeEnd = padRangeLimit & mask;
            if (useMaskedPadRangeStart == (pad & 0x0000ffff)
                    && useMaskedPadRangeEnd == (padRangeLimit & 0x0000ffff)) {
                useMaskedPadRange = true;
                if (useMaskedPadRangeStart > useMaskedPadRangeEnd) {
                    int tmp = useMaskedPadRangeEnd;
                    useMaskedPadRangeEnd = useMaskedPadRangeStart;
                    useMaskedPadRangeStart = tmp;
                }
            } else {
                useNonMaskedSinglePadValue = true;
                nonMaskedSinglePadValue = pad;
            }
        }

        String vPhotometricInterpretation = getStringOrDefault(
                image.getPhotometricInterpretation(), "MONOCHROME2");

        if (vPhotometricInterpretation.equals("MONOCHROME2")) {
            isGrayscale = true;
        } else if (vPhotometricInterpretation.equals("MONOCHROME1")) {
            isGrayscale = true;
            inverted = true;
        } else if (vPhotometricInterpretation.equals("PALETTE COLOR")) {
            isPaletteColor = true;
        } else if (vPhotometricInterpretation.equals("YBR_FULL")) {
            isYBR = !image.getPhotometricInterpretation().equals("RGB");
        }

        Integer aLargestMonochromePixelValue = image
                .getLargestMonochromePixelValue();

        Attribute aRedPaletteColorLookupTableDescriptor = null;
        Attribute aGreenPaletteColorLookupTableDescriptor = null;
        Attribute aBluePaletteColorLookupTableDescriptor = null;

        largestGray = signed ? 0x00007fff : 0x0000ffff;
        boolean usedLargestMonochromePixelValue = false;
        if (aLargestMonochromePixelValue != null) {
            usedLargestMonochromePixelValue = true;
            largestGray = aLargestMonochromePixelValue;
        }

        boolean usedLargestImagePixelValue = false;
        if (usedLargestMonochromePixelValue == false) {
            Integer aLargestImagePixelValue = image.getLargestImagePixelValue();
            if (aLargestImagePixelValue != null) {
                usedLargestImagePixelValue = true;
                largestGray = aLargestImagePixelValue;
            }
        }

        if (aRedPaletteColorLookupTableDescriptor != null
                && aGreenPaletteColorLookupTableDescriptor != null
                && aBluePaletteColorLookupTableDescriptor != null) {

            if (aRedPaletteColorLookupTableDescriptor != null
                    && aRedPaletteColorLookupTableDescriptor.getVM() == 3) {
                numberOfEntries = aRedPaletteColorLookupTableDescriptor
                        .getIntegerValues()[0];
                if (numberOfEntries == 0)
                    numberOfEntries = 65536;
                firstValueMapped = aRedPaletteColorLookupTableDescriptor
                        .getIntegerValues()[1];
                String pixelPresentation = getStringOrDefault(
                        image.getPixelPresentation(), "");
                if ((usedLargestMonochromePixelValue == false && usedLargestImagePixelValue == false)
                        || image.getPhotometricInterpretation().equals(
                                "PALETTE COLOR")
                        || !(pixelPresentation.equals("COLOR") || pixelPresentation
                                .equals("MIXED"))) {
                    largestGray = firstValueMapped - 1;
                    System.err
                            .println("ourceImage.constructSourceImage(): not treating palette as supplemental, using firstValueMapped "
                                    + firstValueMapped
                                    + " to set largestGray = " + largestGray);
                } else {
                    System.err
                            .println("SourceImage.constructSourceImage(): treating palette as supplemental, largestGray = "
                                    + largestGray);
                }
                bitsPerEntry = aRedPaletteColorLookupTableDescriptor
                        .getIntegerValues()[2];

                if (bitsPerEntry > 0) {
                    Attribute aRedPaletteColorLookupTableData = null;
                    Attribute aGreenPaletteColorLookupTableData = null;
                    Attribute aBluePaletteColorLookupTableData = null;
                    if (aRedPaletteColorLookupTableData != null
                            && aGreenPaletteColorLookupTableData != null
                            && aBluePaletteColorLookupTableData != null) {
                        redTable = aRedPaletteColorLookupTableData
                                .getShortValues();
                        greenTable = aGreenPaletteColorLookupTableData
                                .getShortValues();
                        blueTable = aBluePaletteColorLookupTableData
                                .getShortValues();
                        if (redTable == null || greenTable == null
                                || blueTable == null || redTable.length == 0
                                || greenTable.length == 0
                                || blueTable.length == 0) {
                            System.err
                                    .println("SourceImage.constructSourceImage(): bad color palette (empty data), ignoring");
                            redTable = null;
                            greenTable = null;
                            blueTable = null;
                        }
                    }
                } else {
                    System.err
                            .println("SourceImage.constructSourceImage(): bad color palette (zero value for bitsPerEntry), ignoring");
                }
            }
        }

        {
            try {
                for (GraphicsDevice gd : GraphicsEnvironment
                        .getLocalGraphicsEnvironment().getScreenDevices()) {
                    for (GraphicsConfiguration gc : gd.getConfigurations()) {
                        dstColorSpace = gc.getColorModel().getColorSpace();
                    }
                }
            } catch (java.awt.HeadlessException e) {
                dstColorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            }

            if (dstColorSpace == null) {
                dstColorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            }
        }

        {
            Attribute aICCProfile = null;
            if (aICCProfile != null) {
                byte[] iccProfileBytes = aICCProfile.getByteValues();
                ICC_Profile iccProfile = ICC_Profile
                        .getInstance(iccProfileBytes);
                srcColorSpace = new ICC_ColorSpace(iccProfile);
            } else {
                srcColorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            }
        }

        bufferedImageSource = null;

        int nframepixels = width * height;
        int nframesamples = nframepixels * samples;
        int nsamples = nframesamples * nframes;
        int npixels = nframepixels * nframes;
        if ((isGrayscale || isPaletteColor) && samples == 1 && depth > 8
                && depth <= 16) {
            bufferedImageSource = null;
            short[] pixelData = image.getPixelData();
            if (bufferedImageSource == null) {
                short data[] = pixelData;
                if (signed) {
                    imgs = null;
                    bufferedImageSource = new SignedShortGrayscaleBufferedImageSource(
                            data, width, height, mask, signbit, extend,
                            largestGray);
                } else {
                    imgs = null;
                    bufferedImageSource = new UnsignedShortGrayscaleBufferedImageSource(
                            data, width, height, mask, largestGray);
                }
            }
        } else if ((isGrayscale || isPaletteColor) && samples == 1
                && depth <= 8 && depth > 1) {
            byte data[] = null;
            short[] pixelData = image.getPixelData();
            short sdata[] = pixelData;
            data = new byte[nsamples];
            int slen = nsamples / 2;
            int scount = 0;
            int count = 0;
            while (scount < slen) {
                int value = ((int) sdata[scount++]) & 0xffff;
                int value1 = value & mask;
                int nonextendedvalue1 = value1;
                if (signed && (value1 & signbit) != 0)
                    value1 = value1 | extend;
                data[count++] = (byte) value1;
                if (nonextendedvalue1 < useMaskedPadRangeStart
                        || nonextendedvalue1 > useMaskedPadRangeEnd) {
                    if (value1 > imgMax && value1 <= largestGray)
                        imgMax = value1;
                    if (value1 < imgMin)
                        imgMin = value1;
                }
                int value2 = (value >> 8) & mask;
                int nonextendedvalue2 = value2;
                if (signed && (value2 & signbit) != 0)
                    value2 = value2 | extend;
                data[count++] = (byte) value2;
                if (nonextendedvalue2 < useMaskedPadRangeStart
                        || nonextendedvalue2 > useMaskedPadRangeEnd) {
                    if (value2 > imgMax && value2 <= largestGray)
                        imgMax = value2;
                    if (value2 < imgMin)
                        imgMin = value2;
                }
            }
            imgs = null;
            bufferedImageSource = new ByteGrayscaleBufferedImageSource(data,
                    width, height);
        } else if (isGrayscale && samples == 1 && depth == 1) {
            imgs = new BufferedImage[nframes];
            IndexColorModel colorModel = null;
            {
                byte[] r = { (byte) 0, (byte) 255 };
                byte[] g = { (byte) 0, (byte) 255 };
                byte[] b = { (byte) 0, (byte) 255 };
                colorModel = new IndexColorModel(1 /* bits */, 2 /* size */, r,
                        g, b);
            }
            imgMin = 0;
            imgMax = 1;

            short[] pixelData = image.getPixelData();
            int wi = 0;
            int bitsRemaining = 0;
            int word = 0;
            boolean badBitOrder = false;

            short data[] = pixelData;
            for (int f = 0; f < nframes; ++f) {
                imgs[f] = new BufferedImage(width, height,
                        BufferedImage.TYPE_BYTE_BINARY, colorModel);
                Raster raster = imgs[f].getData();
                SampleModel sampleModel = raster.getSampleModel();
                DataBuffer dataBuffer = raster.getDataBuffer();
                for (int row = 0; row < height; ++row) {
                    for (int column = 0; column < width; ++column) {
                        if (bitsRemaining <= 0) {
                            word = data[wi++] & 0xffff;
                            bitsRemaining = 16;
                        }
                        int bit = badBitOrder ? (word & 0x8000)
                                : (word & 0x0001);
                        if (bit != 0) {
                            sampleModel.setSample(column, row, 0/* bank */, 1,
                                    dataBuffer);
                        }
                        word = badBitOrder ? (word << 1) : (word >>> 1);
                        --bitsRemaining;
                    }
                }
                imgs[f].setData(raster);
            }
        } else if (!isGrayscale && samples == 3 && depth <= 8 && depth > 1) {
            byte data[] = null;
            ByteBuffer buffer = null;
            short[] pixelData = image.getPixelData();
            short sdata[] = pixelData;
            data = new byte[nsamples];
            int slen = nsamples / 2;
            int scount = 0;
            int count = 0;
            while (scount < slen) {
                int value = ((int) sdata[scount++]) & 0xffff;
                int value1 = value & 0xff;
                data[count++] = (byte) value1;
                int value2 = (value >> 8) & 0xff;
                data[count++] = (byte) value2;
            }
            imgs = null;

            if (byplane) {
                if (buffer != null) {
                    bufferedImageSource = new BandInterleavedByteRGBBufferedImageSource(
                            buffer, width, height, srcColorSpace);
                } else {
                    bufferedImageSource = new BandInterleavedByteRGBBufferedImageSource(
                            data, width, height, srcColorSpace);
                }
            } else {
                if (buffer != null) {
                    bufferedImageSource = new PixelInterleavedByteRGBBufferedImageSource(
                            buffer, width, height, srcColorSpace);
                } else {
                    bufferedImageSource = new PixelInterleavedByteRGBBufferedImageSource(
                            data, width, height, srcColorSpace);
                }
            }
        } else if (isGrayscale && samples == 1 && depth == 32) {
            short[] pixelData = image.getPixelData();
            throw new DicomException(
                    "Unsupported 32 bit grayscale image encoding");
        } else if (isGrayscale && samples == 1 && depth == 64) {
            short[] pixelData = image.getPixelData();
            throw new DicomException(
                    "Unsupported 64 bit grayscale image encoding");
        } else {
            throw new DicomException(
                    "Unsupported image encoding: Photometric Interpretation = \""
                            + vPhotometricInterpretation + "\", samples = "
                            + samples + "\", Bits Allocated = " + depth
                            + ", Bits Stored = " + stored);
        }
        if (hasPad) {
            backgroundValue = pad;
        } else if (isGrayscale) {
            backgroundValue = inverted ? (signed ? (mask >> 1) : mask)
                    : (signed ? (((mask >> 1) + 1) | extend) : 0);
        } else {
            backgroundValue = 0;
        }
    }
    

    
    /**
     * 
     * Buffers image details
     *
     */
    private abstract class BufferedImageSource {
        protected int nframesamples;
        private int cachedIndex;
        BufferedImage cachedBufferedImage;

        BufferedImageSource(int nframesamples) {
            this.nframesamples = nframesamples;
            cachedIndex = -1;
            cachedBufferedImage = null;
        }

        protected void finalize() throws Throwable {
            cachedBufferedImage = null;
            super.finalize();
        }

        public BufferedImage getBufferedImage(int index) {
            if (index != cachedIndex) {
                cachedBufferedImage = getUncachedBufferedImage(index);
                if (cachedBufferedImage != null) {
                    cachedIndex = index;
                } else {
                    cachedIndex = -1;
                }
            }
            return cachedBufferedImage;
        }

        abstract public BufferedImage getUncachedBufferedImage(int index);

        public double getMinimumPixelValueOfMostRecentBufferedImage(
                double oldMin) {
            return oldMin;
        }

        public double getMaximumPixelValueOfMostRecentBufferedImage(
                double oldMax) {
            return oldMax;
        }
    }
    
    /**
     * 
     * Buffer for image pixel height and width
     *
     */
    private abstract class ShortBufferedImageSource extends BufferedImageSource {
        protected short data[];
        protected ShortBuffer buffer;
        protected boolean minMaxSet;
        protected int imgMin;
        protected int imgMax;

        public double getMinimumPixelValueOfMostRecentBufferedImage(
                double oldMin) {
            return minMaxSet ? ((double) imgMin) : oldMin;
        }

        public double getMaximumPixelValueOfMostRecentBufferedImage(
                double oldMax) {
            return minMaxSet ? ((double) imgMax) : oldMax;
        }

        ShortBufferedImageSource(short data[], int width, int height,
                int samples) {
            super(width * height * samples);
            this.data = data;
            minMaxSet = false;
        }

        ShortBufferedImageSource(ShortBuffer buffer, int width, int height,
                int samples) {
            super(width * height * samples);
            this.buffer = buffer;
            minMaxSet = false;
        }

        protected void finalize() throws Throwable {
            data = null;
            buffer = null;
            super.finalize();
        }
    }
    
    /**
     * 
     * @param w
     * @param h
     * @param data
     * @param offset
     * @return
     */
    private static BufferedImage createSignedShortGrayscaleImage(int w, int h,
            short data[], int offset) {
        ComponentColorModel cm = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 16 },
                false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);

        ComponentSampleModel sm = new ComponentSampleModel(
                DataBuffer.TYPE_USHORT, w, h, 1, w, new int[] { 0 });

        DataBuffer buf = new DataBufferUShort(data, w, offset);

        WritableRaster wr = Raster.createWritableRaster(sm, buf,
                new Point(0, 0));
        
        BufferedImage unscaledImage = new BufferedImage(cm, wr, true, null);
//        System.out.println("Components before scaling: " + unscaledImage.getColorModel().getNumComponents());
//        BufferedImage scaledImage = applyScaleFactor(unscaledImage, cm, wr); // this does zooming... Brad
//        System.out.println("Components after scaling: " + scaledImage.getColorModel().getNumComponents());

        return unscaledImage;
    }
    
    public static BufferedImage applyScaleFactor(BufferedImage image, ComponentColorModel cm, WritableRaster wr){
    	int scaleX = (int) (image.getWidth() * 1.25);
    	int scaleY = (int) (image.getHeight() * 1.25);
    	Image newImg = image.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
    	BufferedImage i = new BufferedImage(cm, wr, true, null);
    	i.getGraphics().drawImage(newImg, 0, 0 , null);
    	return i;
    }
    
    /**
     * 
     * 
     *
     */
    private class SignedShortGrayscaleBufferedImageSource extends
            ShortBufferedImageSource {
        protected int mask;
        protected int signbit;
        protected int extend;
        protected int largestGray;

        SignedShortGrayscaleBufferedImageSource(short data[], int width,
                int height, int mask, int signbit, int extend, int largestGray) {
            super(data, width, height, 1);
            this.mask = mask;
            this.signbit = signbit;
            this.extend = extend;
            this.largestGray = largestGray;
            imgMin = 0x00007fff;
            imgMax = 0xffff8000;
        }

        SignedShortGrayscaleBufferedImageSource(ShortBuffer buffer, int width,
                int height, int mask, int signbit, int extend, int largestGray) {
            super(buffer, width, height, 1);
            this.mask = mask;
            this.signbit = signbit;
            this.extend = extend;
            this.largestGray = largestGray;
            imgMin = 0x00007fff;
            imgMax = 0xffff8000;
        }

        public BufferedImage getUncachedBufferedImage(int index) {
            short[] useData;
            int useOffset;
            if (data == null) {
                useData = new short[nframesamples];
                int position = nframesamples * index;
                buffer.position(position);
                buffer.get(useData);
                useOffset = 0;
            } else {
                useData = data;
                useOffset = nframesamples * index;
            }

            short[] newData = new short[nframesamples];
            for (int i = useOffset, j = 0; j < nframesamples; ++i, ++j) {
                boolean isPaddingValue = false;
                short unMaskedValue = useData[i];
                if (useNonMaskedSinglePadValue
                        && unMaskedValue == ((short) nonMaskedSinglePadValue)) {
                    isPaddingValue = true;
                }
                int value = ((int) unMaskedValue) & mask;
                int nonextendedvalue = value;
                if ((value & signbit) != 0)
                    value = value | extend;
                newData[j] = (short) value;
                if (useMaskedPadRange
                        && (nonextendedvalue >= useMaskedPadRangeStart && nonextendedvalue <= useMaskedPadRangeEnd)) {
                    isPaddingValue = true;
                }
                if (!isPaddingValue) {
                    if (value > imgMax && value <= largestGray) {
                        imgMax = value;
                    }
                    if (value < imgMin) {
                        imgMin = value;
                    }
                }
            }
            minMaxSet = true;
            return createSignedShortGrayscaleImage(width, height, newData, 0);
        }
    }
    
    /**
     * 
     * @param w
     * @param h
     * @param data
     * @param offset
     * @return
     */
    private static BufferedImage createUnsignedShortGrayscaleImage(int w,
            int h, short data[], int offset) {
        ComponentColorModel cm = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 16 },
                false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);

        ComponentSampleModel sm = new ComponentSampleModel(
                DataBuffer.TYPE_USHORT, w, h, 1, w, new int[] { 0 });

        DataBuffer buf = new DataBufferUShort(data, w, offset);

        WritableRaster wr = Raster.createWritableRaster(sm, buf,
                new Point(0, 0));

        return new BufferedImage(cm, wr, true, null);
    }
    
    /**
     * 
     * 
     *
     */
    private class UnsignedShortGrayscaleBufferedImageSource extends
            ShortBufferedImageSource {
        protected int mask;
        protected int largestGray;

        UnsignedShortGrayscaleBufferedImageSource(short data[], int width,
                int height, int mask, int largestGray) {
            super(data, width, height, 1);
            this.mask = mask;
            this.largestGray = largestGray;
            imgMin = 0x0000ffff;
            imgMax = 0x00000000;
        }

        UnsignedShortGrayscaleBufferedImageSource(ShortBuffer buffer,
                int width, int height, int mask, int largestGray) {
            super(buffer, width, height, 1);
            this.mask = mask;
            imgMin = 0x0000ffff;
            imgMax = 0x0000ffff;
        }

        public BufferedImage getUncachedBufferedImage(int index) {
            short[] useData;
            int useOffset;
            if (data == null) {
                useData = new short[nframesamples];
                int position = nframesamples * index;
                buffer.position(position);
                buffer.get(useData);
                useOffset = 0;
            } else {
                useData = data;
                useOffset = nframesamples * index;
            }

            short[] newData = new short[nframesamples];
            for (int i = useOffset, j = 0; j < nframesamples; ++i, ++j) {
                boolean isPaddingValue = false;
                short unmaskedValue = useData[i];
                if (useNonMaskedSinglePadValue
                        && unmaskedValue == ((short) nonMaskedSinglePadValue)) {
                    isPaddingValue = true;
                }
                int value = ((int) unmaskedValue) & mask;
                newData[j] = (short) value;
                if (useMaskedPadRange
                        && (value >= useMaskedPadRangeStart && value <= useMaskedPadRangeEnd)) {
                    isPaddingValue = true;
                }
                if (!isPaddingValue) {
                    if (value > imgMax && value <= largestGray) {
                        imgMax = value;
                    }
                    if (value < imgMin) {
                        imgMin = value;
                    }
                }
            }
            minMaxSet = true;
            return createUnsignedShortGrayscaleImage(width, height, newData, 0);
        }
    }
    
    /**
     * 
     * 
     *
     */
    private abstract class ByteBufferedImageSource extends BufferedImageSource {
        protected byte data[];
        protected ByteBuffer buffer;

        ByteBufferedImageSource(byte data[], int width, int height, int samples) {
            super(width * height * samples);
            this.data = data;
        }

        ByteBufferedImageSource(ByteBuffer buffer, int width, int height,
                int samples) {
            super(width * height * samples);
            this.buffer = buffer;
        }

        protected void finalize() throws Throwable {
            data = null;
            buffer = null;
            super.finalize();
        }
    }
    
    /**
     * 
     * @param w
     * @param h
     * @param data
     * @param offset
     * @return
     */
    private static BufferedImage createByteGrayscaleImage(int w, int h,
            byte data[], int offset) {
        ComponentColorModel cm = new ComponentColorModel(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), new int[] { 8 },
                false, false, Transparency.OPAQUE, DataBufferShort.TYPE_BYTE);

        ComponentSampleModel sm = new ComponentSampleModel(
                DataBuffer.TYPE_BYTE, w, h, 1, w, new int[] { 0 });

        DataBuffer buf = new DataBufferByte(data, w, offset);

        WritableRaster wr = Raster.createWritableRaster(sm, buf,
                new Point(0, 0));

        return new BufferedImage(cm, wr, true, null);
    }
    
    /**
     * 
     * 
     *
     */
    private class ByteGrayscaleBufferedImageSource extends
            ByteBufferedImageSource {
        ByteGrayscaleBufferedImageSource(byte data[], int width, int height) {
            super(data, width, height, 1);
        }

        ByteGrayscaleBufferedImageSource(ByteBuffer buffer, int width,
                int height) {
            super(buffer, width, height, 1);
        }

        public BufferedImage getUncachedBufferedImage(int index) {
            byte[] useData;
            int useOffset;
            if (data == null) {
                useData = new byte[nframesamples];
                int position = nframesamples * index;
                buffer.position(position);
                buffer.get(useData);
                useOffset = 0;
            } else {
                useData = data;
                useOffset = nframesamples * index;
            }
            return createByteGrayscaleImage(width, height, useData, useOffset);
        }
    }
    
    /**
     * 
     * @param w
     * @param h
     * @param data
     * @param offset
     * @param colorSpace
     * @return
     */
    private static BufferedImage createBandInterleavedByteRGBImage(int w,
            int h, byte data[], int offset, ColorSpace colorSpace) {

        ComponentColorModel cm = new ComponentColorModel(colorSpace, new int[] {
                8, 8, 8 }, false, false, Transparency.OPAQUE,
                DataBuffer.TYPE_BYTE);

        ComponentSampleModel sm = new ComponentSampleModel(
                DataBuffer.TYPE_BYTE, w, h, 1, w, new int[] { 0, w * h,
                        w * h * 2 });

        DataBuffer buf = new DataBufferByte(data, w, offset);

        WritableRaster wr = Raster.createWritableRaster(sm, buf,
                new Point(0, 0));

        return new BufferedImage(cm, wr, true, null);

    }
    
    /**
     * 
     * 
     *
     */
    private class BandInterleavedByteRGBBufferedImageSource extends
            ByteBufferedImageSource {
        ColorSpace colorSpace;

        BandInterleavedByteRGBBufferedImageSource(byte data[], int width,
                int height, ColorSpace colorSpace) {
            super(data, width, height, 3);
            this.colorSpace = colorSpace;
        }

        BandInterleavedByteRGBBufferedImageSource(ByteBuffer buffer, int width,
                int height, ColorSpace colorSpace) {
            super(buffer, width, height, 3);
            this.colorSpace = colorSpace;
        }

        public BufferedImage getUncachedBufferedImage(int index) {
            byte[] useData;
            int useOffset;
            if (data == null) {
                useData = new byte[nframesamples];
                int position = nframesamples * index;
                buffer.position(position);
                buffer.get(useData);
                useOffset = 0;
            } else {
                useData = data;
                useOffset = nframesamples * index;
            }
            return createBandInterleavedByteRGBImage(width, height, useData,
                    useOffset, colorSpace);
        }

        public double getMinimumPixelValueOfMostRecentBufferedImage(
                double oldMin) {
            return 0;
        }

        public double getMaximumPixelValueOfMostRecentBufferedImage(
                double oldMax) {
            return 255;
        }
    }
    
    /**
     * 
     * @param w
     * @param h
     * @param data
     * @param offset
     * @param colorSpace
     * @return
     */
    private static BufferedImage createPixelInterleavedByteRGBImage(int w,
            int h, byte data[], int offset, ColorSpace colorSpace) {
        ComponentColorModel cm = new ComponentColorModel(colorSpace, new int[] {
                8, 8, 8 }, false, false, Transparency.OPAQUE,
                DataBuffer.TYPE_BYTE);

        ComponentSampleModel sm = new ComponentSampleModel(
                DataBuffer.TYPE_BYTE, w, h, 3, w * 3, new int[] { 0, 1, 2 });

        DataBuffer buf = new DataBufferByte(data, w, offset);

        WritableRaster wr = Raster.createWritableRaster(sm, buf,
                new Point(0, 0));

        return new BufferedImage(cm, wr, true, null);
    }
    
    /**
     * 
     * 
     *
     */
    private class PixelInterleavedByteRGBBufferedImageSource extends
            ByteBufferedImageSource {
        ColorSpace colorSpace;

        PixelInterleavedByteRGBBufferedImageSource(byte data[], int width,
                int height, ColorSpace colorSpace) {
            super(data, width, height, 3);
            this.colorSpace = colorSpace;
        }

        PixelInterleavedByteRGBBufferedImageSource(ByteBuffer buffer,
                int width, int height, ColorSpace colorSpace) {
            super(buffer, width, height, 3);
            this.colorSpace = colorSpace;
        }

        public BufferedImage getUncachedBufferedImage(int index) {
            byte[] useData;
            int useOffset;
            if (data == null) {
                useData = new byte[nframesamples];
                int position = nframesamples * index;
                buffer.position(position);
                buffer.get(useData);
                useOffset = 0;
            } else {
                useData = data;
                useOffset = nframesamples * index;
            }
            return createPixelInterleavedByteRGBImage(width, height, useData,
                    useOffset, colorSpace);
        }

        public double getMinimumPixelValueOfMostRecentBufferedImage(
                double oldMin) {
            return 0;
        }

        public double getMaximumPixelValueOfMostRecentBufferedImage(
                double oldMax) {
            return 255;
        }

    }
    
    /**
     * 
     * @param value
     * @param default_value
     * @return
     */
    private int getIntOrDefault(Integer value, int default_value) {
        if (value == null)
            return default_value;

        return value;
    }
    
    /**
     * 
     * @param string
     * @param default_string
     * @return
     */
    private String getStringOrDefault(String string, String default_string) {
        if (string == null)
            return default_string;
        return string;
    }

    /***/
    public BufferedImage getBufferedImage() {
        return getBufferedImage(0);
    }

    /***/
    public BufferedImage getBufferedImage(int i) {
        BufferedImage img = null;
        if (bufferedImageSource == null) {
            img = (imgs == null || i < 0 || i >= imgs.length) ? null : imgs[i];
        } else {
            img = bufferedImageSource.getBufferedImage(i);

            if (img.getColorModel().getColorSpace().getType() == ColorSpace.TYPE_RGB
                    && srcColorSpace != null
                    && dstColorSpace != null
                    && srcColorSpace != dstColorSpace) {
                System.err
                        .println("SourceImage.getBufferedImage(): have color image with different source and destination color spaces - converting");
                try {
                    System.err
                            .println("SourceImage.getBufferedImage(): System.getProperty(\"sun.java2d.cmm\") = "
                                    + System.getProperty("sun.java2d.cmm"));
                    ColorConvertOp cco = new ColorConvertOp(srcColorSpace,
                            dstColorSpace, new RenderingHints(
                                    RenderingHints.KEY_COLOR_RENDERING,
                                    RenderingHints.VALUE_COLOR_RENDER_QUALITY));
                    BufferedImage convertedImg = cco.filter(img, null);
                    // System.err.println("SourceImage.getBufferedImage(): have converted color image ="+convertedImg);
                    img = convertedImg;
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
            imgMin = bufferedImageSource
                    .getMinimumPixelValueOfMostRecentBufferedImage(imgMin);
            imgMax = bufferedImageSource
                    .getMaximumPixelValueOfMostRecentBufferedImage(imgMax);
        }
        return img;
    }

    /***/
    public int getNumberOfBufferedImages() {
        return nframes;
    }

    /***/
    public int getWidth() {
        return width;
    }

    /***/
    public int getHeight() {
        return height;
    }

    /***/
    public Dimension getDimension() {
        return new Dimension(width, height);
    }

    /**
     * @return the minimum pixel value, excluding any pixels in the padding
     *         value range
     */
    public double getMinimum() {
        return imgMin;
    }

    /**
     * @return the maximum pixel value, excluding any pixels in the padding
     *         value range
     */
    public double getMaximum() {
        return imgMax;
    }

    /***/
    public int getMaskValue() {
        return mask;
    }

    /***/
    public boolean isSigned() {
        return signed;
    }

    /***/
    public boolean isInverted() {
        return inverted;
    }

    /***/
    public boolean isPadded() {
        return hasPad;
    }

    /***/
    public int getPadValue() {
        return pad;
    }

    /***/
    public int getPadRangeLimit() {
        return padRangeLimit;
    }

    /***/
    public int getBackgroundValue() {
        return backgroundValue;
    }

    /***/
    public boolean isGrayscale() {
        return isGrayscale;
    }

    /***/
    public boolean isYBR() {
        return isYBR;
    }

    /***/
    public String getTitle() {
        return title;
    }

    /***/
    public int getNumberOfFrames() {
        return nframes;
    }

    /***/
    public int getPaletteColorLargestGray() {
        return largestGray;
    }

    /***/
    public int getPaletteColorFirstValueMapped() {
        return firstValueMapped;
    }

    /***/
    public int getPaletteColorNumberOfEntries() {
        return numberOfEntries;
    }

    /***/
    public int getPaletteColorBitsPerEntry() {
        return bitsPerEntry;
    }

    /***/
    public short[] getPaletteColorRedTable() {
        return redTable;
    }

    /***/
    public short[] getPaletteColorGreenTable() {
        return greenTable;
    }

    /***/
    public short[] getPaletteColorBlueTable() {
        return blueTable;
    }

    /***/
    public SUVTransform getSUVTransform() {
        return suvTransform;
    }

    /***/
    public RealWorldValueTransform getRealWorldValueTransform() {
        return realWorldValueTransform;
    }

    /***/
    public ModalityTransform getModalityTransform() {
        return modalityTransform;
    }

    /***/
    public VOITransform getVOITransform() {
        return voiTransform;
    }

    /***/
    public DisplayShutter getDisplayShutter() {
        return displayShutter;
    }

    /***/
    public Overlay getOverlay() {
        return overlay;
    }
}
