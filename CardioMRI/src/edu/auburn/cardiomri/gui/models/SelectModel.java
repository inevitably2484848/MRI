package edu.auburn.cardiomri.gui.models;

import java.util.ArrayList;
import java.util.List;

import edu.auburn.cardiomri.datastructure.Group;
import edu.auburn.cardiomri.datastructure.Study;

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

    public SelectModel(Study study) {
        super();
        this.study = study;
        shortAxis = twoChamber = fourChamber = -1;
    }

    public String[] getSeriesDescriptions() {
        List<String> descriptions = new ArrayList<String>(study.getGroups()
                .size());

        for (Group group : study.getGroups()) {
            descriptions.add(group.getSeriesDescription());
        }

        return descriptions.toArray(new String[descriptions.size()]);
    }

    /**
     * Validate the SA, 2CH and 4CH indices, then notifyObservers(study)
     * 
     * @return true if observers were notified, false if indices were invalid
     */
    public boolean validateStudy() {
        // TODO Auto-generated method stub
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
            setChanged();
            notifyObservers(study);
            return true;
        } else {
            return false;
        }
    }

}