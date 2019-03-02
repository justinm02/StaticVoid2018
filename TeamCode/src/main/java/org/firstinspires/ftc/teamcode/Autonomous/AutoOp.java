package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Robot.BuggleCam;
import org.firstinspires.ftc.teamcode.Robot.Intake;

import java.io.File;

@SuppressWarnings("Duplicates")
public abstract class AutoOp extends LinearOpMode {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    protected ElapsedTime runtime;
    private BNO055IMU imu;
    protected DistanceSensor distanceSensor;
    private DcMotorEx[] motors;

    private double desiredHeading;

    private static final double WHEEL_DIAMETER = 4, WHEEL_TICKS_PER_REV = 560, LIFT_TICKS_PER_REV = 2240, LIFT_DIAMETER = 2.165, LIFT_RATIO = 1;
    private static final double COUNTS_PER_INCH = (WHEEL_TICKS_PER_REV/(WHEEL_DIAMETER * Math.PI));
    private static final double LIFT_COUNTS_PER_INCH = (LIFT_TICKS_PER_REV/(Math.PI * LIFT_DIAMETER * LIFT_RATIO));

    protected Intake intake;
    protected BuggleCam cam;

    private File surprise = new File("/sdcard/FIRST/blocks/sounds/Africa by Toto.mp3");

    boolean rotatedLeft = false, rotatedRight = false;

    public void initialize() {
        runtime = new ElapsedTime();

        //Instantiating the Motors
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        motors = new DcMotorEx[] {this.rearLeft, this.rearRight, this.frontLeft, this.frontRight};

        intake = new Intake(hardwareMap.get(DcMotorEx.class, "lift"),
                hardwareMap.get(DcMotorEx.class, "slide"),
                hardwareMap.get(DcMotorEx.class, "depositor"),
                hardwareMap.get(DcMotorEx.class, "intakeLift"),
                hardwareMap.servo.get("basket"),
                hardwareMap.get(CRServo.class, "intake"),
                hardwareMap.servo.get("trapdoor"),
                hardwareMap.servo.get("phoneMount"));
        intake.setTelemetry(this.telemetry);

        //Reverse the Right Motors
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        imu.initialize(parameters);

        distanceSensor = hardwareMap.get(DistanceSensor.class, "distanceSensor");

        //Instantiates a Camera Object for use with Mineral Detection
        cam = new BuggleCam(this.telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        //Set the encoder position back to 0
        resetEncoders();
        intake.resetEncoders();
        intake.lockIntake();
        intake.markerDepositor(0);
    }

    //Loop required for all Autonomous modes
    @Override
    public abstract void runOpMode();

    //Descend robot from Lander, move off the hook
    protected void lowerBot() {
        lowerBot(-.3);
    }

    protected void lowerBot(double power) {
        intake.liftPosition(6000);
    }

    //Locates the gold mineral from one of the three given locations
    protected void prospect(){
        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();

        runtime.reset();
        resetHeading();
        rotatedLeft = false;
        rotatedRight = false;
        rotatePhoneMount(.45);

        while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL && runtime.seconds() < 4 && opModeIsActive()) {
            cam.betterUpdate(telemetry);
            //telemetry.update();
            if(runtime.seconds() > 2 && !rotatedRight) {
                rotatePhoneMount(.65);
                rotatedRight = true;
            }
        }
        if(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL)
            rotatedLeft = true;
        if(rotatedRight && !rotatedLeft)
            cam.setGoldPosition(BuggleCam.GOLD_POSITION.RIGHT);
        else if(rotatedRight)
            cam.setGoldPosition(BuggleCam.GOLD_POSITION.LEFT);
        cam.stopTFOD();

    }

    protected void dispenseMarker() {
        intake.markerDepositor(1);
        runtime.reset();
        while(runtime.seconds() < 1 && opModeIsActive()) {
            telemetry.addData("Status", "Depositing Marker");
            telemetry.update();
        }
        intake.markerDepositor(0);
    }

    protected void dispenseMarker(int servoPosition) {
        intake.markerDepositor(servoPosition);
    }

    protected void playSurprise() {
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, surprise);
    }

    public void resetEncoders() {
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
    }

    public void resetHeading() {
        desiredHeading = currentAngle();
    }

    private double currentAngle() {
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public void resetPosition() {
        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setPower(-.2);
        }
        while((int) distanceSensor.getDistance(DistanceUnit.CM) > 50 && opModeIsActive()) {
            telemetry.addData("Status", "Resetting Position");
            telemetry.addData("Distance", (int) distanceSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
            if((int) distanceSensor.getDistance(DistanceUnit.CM) < 80)
                for(DcMotorEx motor : motors)
                    motor.setPower(-.1);
        }
        for(DcMotorEx motor : motors) {
            motor.setPower(0);
        }
        resetEncoders();
    }

    public void park() {
        //rotatePreciseDegrees(-170);
        runtime.reset();
        while(opModeIsActive() && runtime.seconds() < 3) {
            intake.lift(-.6);
        }
        intake.lift(0);
    }

    public void autoIntake() {

    }

    public void rotatePhoneMount(double position) {
        intake.rotatePhoneMount(position);
    }

    public void rotate(float power) {
        frontLeft.setPower(power);
        rearLeft.setPower(power);
        frontRight.setPower(-power);
        rearRight.setPower(-power);
    }

    public void rotatePreciseDegrees(double degrees) {
        rotatePreciseDegrees(degrees, 0.5f);
    }

    public void rotatePreciseDegrees(double degrees, float power) {
        desiredHeading -= degrees;
        if(desiredHeading <= -180)
            desiredHeading += 360;
        else if(desiredHeading >= 180)
            desiredHeading -= 360;

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while(Math.abs((int) desiredHeading - (int) currentAngle()) > 3 && opModeIsActive()) {
            telemetry.addData("Status", "Rotating (Precise) " + degrees + " Degrees");
            telemetry.addData("Current Angle", currentAngle());
            telemetry.addData("Desired Angle", desiredHeading);
            telemetry.update();
            if(Math.abs(desiredHeading - currentAngle()) <= 10) {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .15f);
                } else {
                    rotate(-power * .15f);
                }
            } else if(Math.abs(desiredHeading - currentAngle()) <= 30) {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .45f);
                } else {
                    rotate(-power * .45f);
                }
            } else {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .7f);
                } else {
                    rotate(-power * .7f);
                }
            }
        }

        rotate(0);

        resetEncoders();
    }

    public void rotateDegrees(double degrees) {
        rotateDegrees(degrees, 0.5f);
    }

    public void rotateDegrees(double degrees, float power) {
        desiredHeading -= degrees;
        if(desiredHeading < -180)
            desiredHeading += 360;
        else if(desiredHeading > 180)
            desiredHeading += 360;

        for(DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        while((int) desiredHeading != (int) currentAngle() && opModeIsActive()) {
            telemetry.addData("Status", "Rotating (Coarse) " + degrees + " Degrees");
            telemetry.addData("Current Angle", currentAngle());
            telemetry.addData("Desired Angle", desiredHeading);
            telemetry.update();
            if(Math.abs(desiredHeading - currentAngle()) <= 3) {
                break;
            } else {
                if(desiredHeading < currentAngle()) {
                    rotate(power * .45f);
                } else {
                    rotate(-power * .45f);
                }
            }
        }

        rotate(0);

        resetEncoders();
    }

    public void longitudinalDistance(double inches) {
        longitudinalDistance(inches, .5);
    }

    public void longitudinalDistance(double inches, double power) {
        frontLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        frontRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearLeft.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        rearRight.setTargetPosition((int) (inches * COUNTS_PER_INCH));
        for (DcMotorEx motor : motors) {
            motor.setPower(power);
        }
        while(frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy() && opModeIsActive()) {
            telemetry.addData("Status", "Moving");
            telemetry.addData("Desired Heading", desiredHeading);
            telemetry.addData("Current Heading", currentAngle());
            //if(Math.abs(frontRight.getTargetPosition() - frontRight.getCurrentPosition()) < 2500) {
            //intake.intakeBasket(-2);
            telemetry.addData("Holding Bucket", intake.intakeBasket(-.15));
            //}
            telemetry.update();

        }
        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }
        //intake.intakeBasket(0);
        resetEncoders();
    }

    public void strafe(double inches, double power, String dir) {
        int ticks = (int) (inches * COUNTS_PER_INCH);
        if (dir.equals("right")) {
            frontLeft.setTargetPosition(ticks);
            rearLeft.setTargetPosition(-ticks);
            frontRight.setTargetPosition(-ticks);
            rearRight.setTargetPosition(ticks);
        }
        else if (dir.equals("left")) {
            frontLeft.setTargetPosition(-ticks);
            rearLeft.setTargetPosition(ticks);
            frontRight.setTargetPosition(ticks);
            rearRight.setTargetPosition(-ticks);
        }

        for(DcMotorEx motor : motors)
            motor.setPower(power);

        while (frontLeft.isBusy() && frontRight.isBusy() && rearLeft.isBusy() && rearRight.isBusy()) {}

        for(DcMotorEx motor : motors)
            motor.setPower(0);
        resetEncoders();
    }

}
