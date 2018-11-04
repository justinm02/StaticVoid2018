package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DriveTrain {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;


    public DriveTrain(DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontLeft, DcMotorEx frontRight) {
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
    }

    public void rotate(float power) {
        frontLeft.setPower(-power);
        rearLeft.setPower(-power);
        frontRight.setPower(power);
        rearRight.setPower(power);
    }

    public void longitudinal(double power) {
        frontRight.setPower(-power);
        frontLeft.setPower(-power);
        rearRight.setPower(power);
        rearLeft.setPower(power);
    }

    public void lateral(double power) {
        frontLeft.setPower(-power);
        rearRight.setPower(-power);
        frontRight.setPower(power);
        rearLeft.setPower(power);
    }

    public void rotateDegrees(int degrees) {

    }

    public void longitudinalDistance(double inches) {

    }

    public void lateralDistance(double inches) {

    }
}
