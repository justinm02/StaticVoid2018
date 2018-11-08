package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DriveTrain {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;

    private static final double COUNTS_PER_REVOLUTION = 537.6;
    private static final double GEAR_TO_MOTOR_RATIO = 0.5;
    private static final double WHEEL_DIAMETER = 4;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_REVOLUTION * GEAR_TO_MOTOR_RATIO) / (WHEEL_DIAMETER * 3.14159);

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
        frontRight.setPower(power);
        frontLeft.setPower(power);
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
        frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
    }

    public void lateralDistance(double inches) {
        frontLeft.setTargetPosition(-(int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition(-(int) (inches * COUNTS_PER_INCH));
    }
}
