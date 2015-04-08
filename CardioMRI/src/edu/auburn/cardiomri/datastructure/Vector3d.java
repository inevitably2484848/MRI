package edu.auburn.cardiomri.datastructure;

import java.io.Serializable;

/**
 * A custom Vector class representing a 3D vector.
 * 
 * @author Tony Bernhardt
 *
 */
public class Vector3d implements Serializable {
    private static final long serialVersionUID = -2673685056075348464L;
    private double x, y, z;

    /**
     * Basic constructor for Vector3d
     * 
     * @param x X-component of vector
     * @param y Y-component of vector
     * @param z Z-component of vector
     */
    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * This method performs the cross product of (calling Vector3d) x (vec2)
     * 
     * @param vec2 The Vector3d to cross with the calling Vector3d
     * @return The resultant Vector3d
     */
    public Vector3d cross(Vector3d vec2) {
        double result_x = this.y * vec2.z - this.z * vec2.y;
        double result_y = this.z * vec2.x - this.x * vec2.z;
        double result_z = this.x * vec2.y - this.y * vec2.x;

        return new Vector3d(result_x, result_y, result_z);
    }

    /**
     * This method performs the dot product of the calling Vector3d with the
     * input Vector3d
     * 
     * @param vec2 The Vector3d to dot with the calling Vector3d
     * @return The resultant double value of the dot product
     */
    public double dot(Vector3d vec2) {
        double result = this.x * vec2.x + this.y * vec2.y + this.z * vec2.z;

        return result;
    }

    /**
     * This method calculates the unit vector of the calling Vector3d
     * 
     * @return A Vector3d representing the unit vector of the calling Vector3d
     */
    public Vector3d unit() {
        double length = Math.sqrt(this.x * this.x + this.y * this.y + this.z
                * this.z);

        return new Vector3d(this.x / length, this.y / length, this.z / length);
    }
}
