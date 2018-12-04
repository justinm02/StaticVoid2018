package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class DriveTrain {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx[] motors;
    private Telemetry telemetry;
    private BNO055IMU imu;

    private static final double COUNTS_PER_REVOLUTION = 537.6;
    private static final double GEAR_TO_MOTOR_RATIO = 1/1.18;
    private static final double WHEEL_DIAMETER = 4;
    private static final double COUNTS_PER_INCH = (360/(WHEEL_DIAMETER * Math.PI)) * 1.01;

    private static final double INCHES_PER_DEGREE = (4.8125/90) * Math.PI;
    private static final double COUNTS_PER_DEGREE = COUNTS_PER_INCH * INCHES_PER_DEGREE;

    public DriveTrain(DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontLeft, DcMotorEx frontRight) {
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        motors = new DcMotorEx[] {this.rearLeft, this.rearRight, this.frontLeft, this.frontRight};
    }

    public void progressiveOp() {
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setIMU(BNO055IMU imu) {
        this.imu = imu;
    }

    //Sets position of robot back to zero
    public void resetEncoders() {
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        }


        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

    }

    public void combinedDirections(double xPower, double yPower) {
        frontRight.setPower(yPower + xPower);
        frontLeft.setPower(yPower - xPower);
        rearRight.setPower(yPower + xPower);
        rearLeft.setPower(yPower - xPower);
    }


    //For longitudinal movement, positive distance moves forward
    public void longitudinal(double power) {
        frontRight.setPower(power);
        frontLeft.setPower(power);
        rearRight.setPower(power);
        rearLeft.setPower(power);
    }

    public void longitudinalDistance(double inches) {
        longitudinalDistance(inches, 0.2);
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
            if(telemetry != null) {
                telemetry.addData("Front Right Encoder", frontRight.getCurrentPosition());
                telemetry.addData("Front Left Encoder", frontLeft.getCurrentPosition());
                telemetry.addData("Rear Right Encoder", rearRight.getCurrentPosition());
                telemetry.addData("Rear Left Encoder", rearLeft.getCurrentPosition());
                telemetry.update();
            }
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
        int desiredAngle = (int) imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle - (int) degrees;

        /*frontLeft.setTargetPosition((int) (degrees * COUNTS_PER_DEGREE));
        rearLeft.setTargetPosition((int) (degrees * COUNTS_PER_DEGREE));
        frontRight.setTargetPosition(-(int) (degrees * COUNTS_PER_DEGREE));
        rearRight.setTargetPosition(-(int) (degrees * COUNTS_PER_DEGREE));

        for(DcMotorEx motor : motors) {
            motor.setPower(power);
        }

        while(frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy()) {
            *//*if(telemetry != null) {
                telemetry.addData("Front Right Encoder", frontRight.getCurrentPosition());
                telemetry.addData("Front Left Encoder", frontLeft.getCurrentPosition());
                telemetry.addData("Rear Right Encoder", rearRight.getCurrentPosition());
                telemetry.addData("Rear Left Encoder", rearLeft.getCurrentPosition());
            }
            telemetry.update();*//**//*
         *//*
            telemetry.addData("Current Angle", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            telemetry.addData("Desired Angle", desiredAngle);
            telemetry.update();
        }*/

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while(desiredAngle != (int) imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) {
            int currentAngle = (int) imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
            telemetry.addData("Current Angle", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            telemetry.addData("Desired Angle", desiredAngle);
            telemetry.update();
            if(Math.abs(desiredAngle - currentAngle) <= 10) {
                if(desiredAngle < currentAngle) {
                    frontLeft.setPower(power * .1);
                    rearLeft.setPower(power * .1);
                    frontRight.setPower(-power * .1);
                    rearRight.setPower(-power * .1);
                } else {
                    frontLeft.setPower(-power * .1);
                    rearLeft.setPower(-power * .1);
                    frontRight.setPower(power * .1);
                    rearRight.setPower(power * .1);
                }
            } else {
                if(desiredAngle < currentAngle) {
                    frontLeft.setPower(power * .3);
                    rearLeft.setPower(power * .3);
                    frontRight.setPower(-power * .3);
                    rearRight.setPower(-power * .3);
                } else {
                    frontLeft.setPower(-power * .3);
                    rearLeft.setPower(-power * .3);
                    frontRight.setPower(power * .3);
                    rearRight.setPower(power * .3);
                }
            }
        }



        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }
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