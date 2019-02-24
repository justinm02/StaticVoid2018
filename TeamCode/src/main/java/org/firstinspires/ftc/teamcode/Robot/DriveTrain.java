package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class DriveTrain {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx[] motors;
    private Telemetry telemetry;
    private BNO055IMU imu;
    private double desiredHeading;

    private static final double WHEEL_DIAMETER = 4;
    private static final double COUNTS_PER_INCH = (360 / (WHEEL_DIAMETER * Math.PI)) * 1.01;

    public DriveTrain(DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontLeft, DcMotorEx frontRight) {
        this.rearLeft = rearLeft;
        this.rearRight = rearRight;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        motors = new DcMotorEx[]{this.rearLeft, this.rearRight, this.frontLeft, this.frontRight};
    }

    public void reverseMotors() {
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public void setIMU(BNO055IMU imu) {
        this.imu = imu;
        desiredHeading = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
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

    //This code is ONLY for Mecanum DriveTrain


    //For some reason, when going up and to the left, frontRight and BackLeft are negative
    public void omniDirectionalMovement(double xPower, double yPower) {
        double magnitude = getMagnitude(xPower, yPower);
        if(yPower > 0 && xPower > 0) {
            frontRight.setPower(magnitude);
            rearLeft.setPower(magnitude);

            frontLeft.setPower(yPower - xPower);
            rearRight.setPower(yPower - xPower);
        } else if(yPower > 0 && xPower < 0) {
            frontLeft.setPower(magnitude);
            rearRight.setPower(magnitude);

            frontRight.setPower(yPower - xPower);
            rearLeft.setPower(yPower - xPower);
        } else if(yPower < 0 && xPower > 0) {
            frontLeft.setPower(-magnitude);
            rearRight.setPower(-magnitude);

            frontRight.setPower(yPower + xPower);
            rearLeft.setPower(yPower + xPower);
        } else if(yPower < 0 && xPower < 0) {
            frontRight.setPower(-magnitude);
            rearLeft.setPower(-magnitude);

            frontLeft.setPower(yPower + xPower);
            rearRight.setPower(yPower + xPower);
        } else if(xPower == 0) {
            frontLeft.setPower(yPower);
            frontRight.setPower(yPower);
            rearLeft.setPower(yPower);
            rearRight.setPower(yPower);
        } else if(yPower == 0) {
            frontRight.setPower(xPower);
            rearLeft.setPower(xPower);

            frontLeft.setPower(-xPower);
            rearRight.setPower(-xPower);
        }

    }

    public void newOmni(double xPower, double yPower /* Takes the exact value of the stick */, double rotatePower, boolean precision) {
        double x = Math.hypot(xPower, yPower);
        double stickAngle = Math.atan2(-yPower, xPower); // desired robot angle from the angle of stick
        double powerAngle = stickAngle - (Math.PI / 4); // conversion for correct power values
        double rightX = rotatePower; // right stick x axis controls turning POSSIBLE SOURCE OF ERROR
        final double leftFrontPower = Range.clip(x * Math.cos(powerAngle) + rightX, -1.0, 1.0);
        final double leftRearPower = Range.clip(x * Math.sin(powerAngle) + rightX, -1.0, 1.0);
        final double rightFrontPower = Range.clip(x * Math.sin(powerAngle) - rightX, -1.0, 1.0);
        final double rightRearPower = Range.clip(x * Math.cos(powerAngle) - rightX, -1.0, 1.0);

        frontLeft.setPower(leftFrontPower * (precision ? 0.2 : .7));
        rearLeft.setPower(leftRearPower * (precision ? 0.2 : .7));
        frontRight.setPower(rightFrontPower * (precision ? 0.2 : .7));
        rearRight.setPower(rightRearPower * (precision ? 0.2 : .7));
    }

    public double getMagnitude(double a, double b) {
        return Math.sqrt((a * a) + (b * b));
    }


    public void resetHeading() {
        desiredHeading = currentAngle();
    }

    //For longitudinal movement, positive distance moves forward
    public void longitudinal(double power) {
        for (DcMotorEx motor : motors) {
            motor.setPower(power);
        }
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
        while (frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy()) {
            telemetry.addData("Status", "Moving");
            telemetry.addData("Desired Heading", desiredHeading);
            telemetry.addData("Current Heading", currentAngle());
            telemetry.update();
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

    public void rotatePreciseDegrees(double degrees) {
        rotatePreciseDegrees(degrees, 0.4f);
    }

    public void rotatePreciseDegrees(double degrees, float power) {
        desiredHeading -= degrees;
        if (desiredHeading <= -180)
            desiredHeading += 360;
        else if (desiredHeading >= 180)
            desiredHeading -= 360;

        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while ((int) desiredHeading != (int) currentAngle()) {
            telemetry.addData("Status", "Rotating (Precise) " + degrees + " Degrees");
            telemetry.addData("Current Angle", currentAngle());
            telemetry.addData("Desired Angle", desiredHeading);
            telemetry.update();
            if (Math.abs(desiredHeading - currentAngle()) <= 10) {
                if (desiredHeading < currentAngle()) {
                    rotate(power * .15f);
                } else {
                    rotate(-power * .15f);
                }
            } else {
                if (desiredHeading < currentAngle()) {
                    rotate(power * .3f);
                } else {
                    rotate(-power * .3f);
                }
            }
        }

        rotate(0);

        resetEncoders();
    }

    public void rotateDegrees(double degrees) {
        rotateDegrees(degrees, 0.4f);
    }

    public void rotateDegrees(double degrees, float power) {
        desiredHeading -= degrees;
        if (desiredHeading <= -360)
            desiredHeading += 180;
        else if (desiredHeading >= 180)
            desiredHeading += 360;

        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while ((int) desiredHeading != (int) currentAngle()) {
            telemetry.addData("Status", "Rotating (Coarse) " + degrees + " Degrees");
            telemetry.addData("Current Angle", currentAngle());
            telemetry.addData("Desired Angle", desiredHeading);
            telemetry.update();
            if (Math.abs(desiredHeading - currentAngle()) <= 3) {
                break;
            } else {
                if (desiredHeading < currentAngle()) {
                    rotate(power * .3f);
                } else {
                    rotate(-power * .3f);
                }
            }
        }

        rotate(0);


        frontRight.setVelocity(4);
        resetEncoders();
    }

    private double currentAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

}