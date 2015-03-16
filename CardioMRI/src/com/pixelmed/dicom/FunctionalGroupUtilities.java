/* Copyright (c) 2001-2014, David A. Clunie DBA Pixelmed Publishing. All rights reserved. */

package com.pixelmed.dicom;

import java.util.Iterator;

/**
 * <p>A class contain useful methods for manipulating Functional Group Sequences.</p>
 *
 * @author	dclunie
 */
public class FunctionalGroupUtilities {
	private static final String identString = "@(#) $Header: /userland/cvs/pixelmed/imgbook/com/pixelmed/dicom/FunctionalGroupUtilities.java,v 1.3 2014/03/11 14:31:59 dclunie Exp $";
	
	/**
	 * <p>Create shared and per-frame functional group sequences if not already present.</p>
	 *
	 * @param	list			an existing (possibly empty) attribute list, if null, a new one will be created; may already shared and per-frame functional group sequences or they will be added
	 * @param	numberOfFrames
	 * return					attribute list with empty per-frame and shared functional group sequences fadded
	 * @exception				DicomException
	 */
	public static AttributeList createFunctionalGroupsIfNotPresent(AttributeList list,int numberOfFrames) {
		if (list == null) {
			list = new AttributeList();
		}

		SequenceAttribute aSharedFunctionalGroupsSequence = (SequenceAttribute)list.get(TagFromName.SharedFunctionalGroupsSequence);
		if (aSharedFunctionalGroupsSequence == null) {
			aSharedFunctionalGroupsSequence = new SequenceAttribute(TagFromName.SharedFunctionalGroupsSequence);
			list.put(aSharedFunctionalGroupsSequence);
			aSharedFunctionalGroupsSequence.addItem(new AttributeList());
		}

		SequenceAttribute aPerFrameFunctionalGroupsSequence = (SequenceAttribute)list.get(TagFromName.PerFrameFunctionalGroupsSequence);
		if (aPerFrameFunctionalGroupsSequence == null) {
			aPerFrameFunctionalGroupsSequence = new SequenceAttribute(TagFromName.PerFrameFunctionalGroupsSequence);
			list.put(aPerFrameFunctionalGroupsSequence);
			for (int f=0; f<numberOfFrames; ++f) {
				aPerFrameFunctionalGroupsSequence.addItem(new AttributeList());
			}
		}

		return list;
	}

	/**
	 * <p>Insert a shared functional group sequence Pixel Value Transformation Sequence entry.</p>
	 *
	 * @param	list			an existing (possibly empty) attribute list, if null, a new one will be created; may already shared and per-frame functional group sequences or they will be added
	 * @param	numberOfFrames
	 * @param	rescaleSlope
	 * @param	rescaleIntercept
	 * @param	rescaleType
	 * return					attribute list with per-frame and shared functional group sequences for VOI added
	 * @exception				DicomException
	 */
	public static AttributeList generatePixelValueTransformationFunctionalGroup(AttributeList list,int numberOfFrames,double rescaleSlope,double rescaleIntercept,String rescaleType) throws DicomException {
		list = createFunctionalGroupsIfNotPresent(list,numberOfFrames);
		SequenceAttribute aSharedFunctionalGroupsSequence = (SequenceAttribute)list.get(TagFromName.SharedFunctionalGroupsSequence);
		AttributeList sharedFunctionalGroupsSequenceList = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(aSharedFunctionalGroupsSequence);

		{
			SequenceAttribute aPixelValueTransformationSequence = new SequenceAttribute(TagFromName.PixelValueTransformationSequence);
			sharedFunctionalGroupsSequenceList.put(aPixelValueTransformationSequence);
			AttributeList itemList = new AttributeList();
			aPixelValueTransformationSequence.addItem(itemList);
			
			{ Attribute a = new DecimalStringAttribute(TagFromName.RescaleSlope); a.addValue(rescaleSlope); itemList.put(a); }
			{ Attribute a = new DecimalStringAttribute(TagFromName.RescaleIntercept); a.addValue(rescaleIntercept); itemList.put(a); }
			{ Attribute a = new LongStringAttribute(TagFromName.RescaleType); a.addValue(rescaleType); itemList.put(a); }
		}

		return list;
	}

	/**
	 * <p>Insert a shared functional group sequence Frame VOI LUT Sequence entry.</p>
	 *
	 * @param	list			an existing (possibly empty) attribute list, if null, a new one will be created; may already shared and per-frame functional group sequences or they will be added
	 * @param	numberOfFrames
	 * @param	windowWidth
	 * @param	windowCenter
	 * @param	voiLUTFunction
	 * return					attribute list with per-frame and shared functional group sequences for VOI added
	 * @exception				DicomException
	 */
	public static AttributeList generateVOILUTFunctionalGroup(AttributeList list,int numberOfFrames,double windowWidth,double windowCenter,String voiLUTFunction) throws DicomException {
		list = createFunctionalGroupsIfNotPresent(list,numberOfFrames);
		SequenceAttribute aSharedFunctionalGroupsSequence = (SequenceAttribute)list.get(TagFromName.SharedFunctionalGroupsSequence);
		AttributeList sharedFunctionalGroupsSequenceList = SequenceAttribute.getAttributeListFromWithinSequenceWithSingleItem(aSharedFunctionalGroupsSequence);

		{
			SequenceAttribute aFrameVOILUTSequence = new SequenceAttribute(TagFromName.FrameVOILUTSequence);
			sharedFunctionalGroupsSequenceList.put(aFrameVOILUTSequence);
			AttributeList itemList = new AttributeList();
			aFrameVOILUTSequence.addItem(itemList);
			
			{ Attribute a = new DecimalStringAttribute(TagFromName.WindowWidth); a.addValue(windowWidth); itemList.put(a); }
			{ Attribute a = new DecimalStringAttribute(TagFromName.WindowCenter); a.addValue(windowCenter); itemList.put(a); }
			{ Attribute a = new CodeStringAttribute(TagFromName.VOILUTFunction); a.addValue(voiLUTFunction); itemList.put(a); }
		}

		return list;
	}
	
	/**
	 * <p>Remove a specified functional group sequences from the shared and per-frame functional group sequences.</p>
	 *
	 * @param	list			an attribute list
	 * @param	numberOfFrames	functionalGroupSequenceTag
	 */
	public static void removeFunctionalGroup(AttributeList list,AttributeTag functionalGroupSequenceTag) {
		SequenceAttribute aPerFrameFunctionalGroupsSequence = (SequenceAttribute)list.get(TagFromName.PerFrameFunctionalGroupsSequence);
		if (aPerFrameFunctionalGroupsSequence != null) {
			int nFrames = aPerFrameFunctionalGroupsSequence.getNumberOfItems();
			int frameNumber = 0;
			Iterator pfitems = aPerFrameFunctionalGroupsSequence.iterator();
			while (pfitems.hasNext()) {
				SequenceItem fitem = (SequenceItem)pfitems.next();
				AttributeList flist = fitem.getAttributeList();
				if (flist != null) {
					flist.remove(functionalGroupSequenceTag);
				}
				++frameNumber;
			}
		}
		SequenceAttribute aSharedFunctionalGroupsSequence = (SequenceAttribute)list.get(TagFromName.SharedFunctionalGroupsSequence);
		if (aSharedFunctionalGroupsSequence != null) {
			// assert aSharedFunctionalGroupsSequence.getNumberOfItems() == 1
			Iterator sitems = aSharedFunctionalGroupsSequence.iterator();
			if (sitems.hasNext()) {
				SequenceItem sitem = (SequenceItem)sitems.next();
				AttributeList slist = sitem.getAttributeList();
				if (slist != null) {
					slist.remove(functionalGroupSequenceTag);
				}
			}
		}
	}
}

