package org.firstinspires.ftc.teamcode.Robot;

public class PID {

    private double kp, ki, kd;
    private double totalError, lastActual;

    public PID(double p, double i, double d) {
        this.kp = p;
        this.ki = i;
        this.kd = d;
    }

    public double update(double actual, double target) {
        double d = -kd * (actual - lastActual);
        totalError += (target - actual);
        lastActual = actual;
        return ((target - actual) * kp) + (ki * totalError) + (d);
    }

}
