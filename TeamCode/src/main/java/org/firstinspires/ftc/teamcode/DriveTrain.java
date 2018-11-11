package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class DriveTrain {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx[] motors;

    private static final double COUNTS_PER_REVOLUTION = 537.6;
    private static final double GEAR_TO_MOTOR_RATIO = 1.0;
    private static final double WHEEL_DIAMETER = 4.0;
    private static final double COUNTS_PER_INCH = (COUNTS_PER_REVOLUTION * GEAR_TO_MOTOR_RATIO) / (WHEEL_DIAMETER * Math.PI);
    private static final double INCHES_PER_DEGREE = (13.35177/90);
    private static final double COUNTS_PER_DEGREE = COUNTS_PER_INCH * INCHES_PER_DEGREE;

    public DriveTrain(DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontLeft, DcMotorEx frontRight) {
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        motors = new DcMotorEx[] {this.rearLeft, this.rearRight, this.frontLeft, this.frontRight};
    }

    //Sets position of robot back to zero
    private void resetEncoders() {
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }


    //For longitudinal movement, positive distance moves forward
    public void longitudinal(double power) {
        frontRight.setPower(power);
        frontLeft.setPower(power);
        rearRight.setPower(power);
        rearLeft.setPower(power);
    }

    public void longitudinalDistance(double inches) {
        longitudinalDistance(inches, 0.3);
    }

    public void longitudinalDistance(double inches, double power) {
        frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));

        for (DcMotorEx motor : motors) {
            motor.setPower(power);
        }

        while(frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy()) {
        }

        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }

        resetEncoders();
    }


    //For lateral movement, positive distance moves right
    public void lateral(double power) {
        frontLeft.setPower(power);
        rearRight.setPower(power);
        frontRight.setPower(-power);
        rearLeft.setPower(-power);
    }

    public void lateralDistance(double inches) {
        lateralDistance(inches, 0.3);
    }

    public void lateralDistance(double inches, double power) {
        frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition(-(int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition(-(int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));

        for (DcMotorEx motor : motors) {
            motor.setPower(power);
        }

        while (frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy()) {

        }

        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }

        resetEncoders();
    }


    //For rotational movement, positive degrees turns clockwise
    public void rotate(float power) {
        frontLeft.setPower(power);
        rearLeft.setPower(power);
        frontRight.setPower(-power);
        rearRight.setPower(-power);
    }

    public void rotateDegrees(double degrees) {
        rotateDegrees(degrees, 0.4);
    }

    public void rotateDegrees(double degrees, double power) {
        frontLeft.setTargetPosition(-(int) (degrees * COUNTS_PER_DEGREE));
        rearLeft.setTargetPosition(-(int) (degrees * COUNTS_PER_DEGREE));
        frontRight.setTargetPosition((int) (degrees * COUNTS_PER_DEGREE));
        rearRight.setTargetPosition((int) (degrees * COUNTS_PER_DEGREE));

        for(DcMotorEx motor : motors) {
            motor.setPower(power);
        }

        while(frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy()) { }

        resetEncoders();
    }
}

/*
 * For our next iteration:
 * Do lots of tests to assure that the inches parameter input into these methods is the distance it will actually move.
 * In other words, compensate with the math in the new versions of the mothods above. Param 2 inches should move 2 inches.
 *
 * Have a forward and backward moving option along with rotation so you're not limited to moving in cardinal directions
 *
 * Write an auto for extending lift, waiting 10ish seconds, and then slowly retracting for raising the bot without manual
 *
 * Fail-safe on the lift??
 */