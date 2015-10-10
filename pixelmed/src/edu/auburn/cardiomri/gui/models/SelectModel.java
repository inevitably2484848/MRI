package edu.auburn.cardiomri.gui.models;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Study;
/**
 * Model for the SelectView. Extends model and determines which image 
 * gets placed in what panel. 
 * 
 * @author Justin
 *
 */
public class SelectModel extends Model {
    protected Study study;
    protected int shortAxis, twoChamber, fourChamber;

    public int getShortAxis() {
        return shortAxis;
    }

    public void setShortAxis(int shortAxis) {
        this.shortAxis = shortAxis;
    }
    
    public int getTwoChamber() {
        return twoChamber;
    }
    
    public void setTwoChamber(int twoChamber) {
        this.twoChamber = twoChamber;
    }
    
    public int getFourChamber() {
        return fourChamber;
    }
    
    public void setFourChamber(int fourChamber) {
        this.fourChamber = fourChamber;
    }
    
    /**
     * Creates an instance of SelectModel
     * 
     * @param study a Study of images
     *
     */
    public SelectModel(Study study) {
        super();
        this.study = study;
        shortAxis = twoChamber = fourChamber = -1;
    }
    
    /**
     * Returns a size of the series 
     * 
     * @return a string size 
     */
    public String[] getSeriesDescriptions() {
        List<String> descriptions = new ArrayList<String>(study.getGroups()
                .size());

        for (Group group : study.getGroups()) {
            descriptions.add(group.getSeriesDescription());
        }

        return descriptions.toArray(new String[descriptions.size()]);
    }

    /**
     * Validate the Short axis, 2 chamber and 4 chamber indices,
     *  then notifyObservers(study)
     * 
     * @return true if observers were notified, false if indices were invalid
     */
    public boolean validateStudy() {
        boolean isValid = true;

        if (getShortAxis() < 0 || getShortAxis() >= study.getGroups().size()) {
            isValid = false;
        }
        if (getTwoChamber() < 0 || getTwoChamber() >= study.getGroups().size()) {
            isValid = false;
        }
        if (getFourChamber() < 0
                || getFourChamber() >= study.getGroups().size()) {
            isValid = false;
        }

        if (getShortAxis() == getTwoChamber()
                || getShortAxis() == getFourChamber()
                || getTwoChamber() == getFourChamber()) {
            isValid = false;
        }

        if (isValid) {
            study.setShortAxis(getShortAxis());
            study.setTwoChamber(getTwoChamber());
            study.setFourChamber(getFourChamber());
            setChanged();
            notifyObservers(study);
            return true;
        } else {
            return false;
        }
    }
}
