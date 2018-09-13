package org.firstinspires.ftc.teamcode;

public class PIDControl {
    private double totalError, lastError, lastTime;
    private double kp, ki, kd;
    private double errorDifferential;

    public PIDControl(double kp, double ki, double kd) {

        this.kp = kp;
        this.ki = ki;
        this.kd = kd;

    }

    public double update(double error, double time) {
        totalError += error;

        errorDifferential = (error - lastError) / (time - lastTime);

        lastError = error;
        lastTime = time;

        return (kp * error) + (ki * error) + (kd * errorDifferential);
    }

}
