package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DriveTrain {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx[] motors;

    private static final double COUNTS_PER_REVOLUTION = 537.6;
    private static final double GEAR_TO_MOTOR_RATIO = 1;
    private static final double WHEEL_DIAMETER = 4;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_REVOLUTION * GEAR_TO_MOTOR_RATIO) / (WHEEL_DIAMETER * 3.14159);
    private static final double INCHES_PER_DEGREE = (13.35177/180);
    private static final double COUNTS_PER_DEGREE = COUNTS_PER_INCH * INCHES_PER_DEGREE;

    public DriveTrain(DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontLeft, DcMotorEx frontRight) {
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        motors = new DcMotorEx[]{rearLeft, rearRight, frontLeft, frontRight};
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
        frontLeft.setPower(power);
        rearRight.setPower(power);
        frontRight.setPower(-power);
        rearLeft.setPower(-power);
    }

    public void rotateDegrees(double degrees) {
        rotateDegrees(degrees, 0.1);
    }

    public void rotateDegrees(double degrees, double power) {
        frontLeft.setTargetPosition(-(int) (degrees * COUNTS_PER_DEGREE));
        rearLeft.setTargetPosition(-(int) (degrees * COUNTS_PER_DEGREE));
        frontRight.setTargetPosition((int) (degrees * COUNTS_PER_DEGREE));
        rearRight.setTargetPosition((int) (degrees * COUNTS_PER_DEGREE));

        for(DcMotorEx motor : motors) {
            motor.setPower(power);
        }

        while(frontLeft.isBusy() || frontRight.isBusy() || rearLeft.isBusy() || rearRight.isBusy()) {}

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void longitudinalDistance(double inches) {
        longitudinalDistance(inches, 0.1);
    }

    public void longitudinalDistance(double inches, double power) {
        for (DcMotorEx motor : motors) {
            motor.setTargetPosition((int) (inches * COUNTS_PER_INCH));
            motor.setPower(power);
        }

        while(frontLeft.isBusy() || frontRight.isBusy() || rearLeft.isBusy() || rearRight.isBusy()) {}

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void lateralDistance(double inches) {
        lateralDistance(inches, 0.1);
    }

    public void lateralDistance(double inches, double power) {
        frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition(-(int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition(-(int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));

        for (DcMotorEx motor : motors) {
            motor.setPower(power);
        }

        while(frontLeft.isBusy() || frontRight.isBusy() || rearLeft.isBusy() || rearRight.isBusy()) {}

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }
}
