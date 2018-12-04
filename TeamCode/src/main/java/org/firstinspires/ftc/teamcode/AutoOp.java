package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.io.File;

public abstract class AutoOp extends LinearOpMode {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift;
    private ElapsedTime runtime;
    private BNO055IMU imu;

    protected DriveTrain driveTrain;
    protected Intake intake;
    protected BuggleCam cam;

    private File surprise = new File("/sdcard/FIRST/blocks/sounds/Africa by Toto.mp3");

    boolean rotatedLeft = false, rotatedRight = false;

    private final double LC = 1680/(Math.PI*3.65625);

    //Order of Operations:
    //
    //1) Lower Robot
    //2) Knock Gold Square
    //3) Place Marker
    //4) Lean on Crater

    public void initialize() {
        runtime = new ElapsedTime();

        //Instantiating the Motors
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        lift = hardwareMap.get(DcMotorEx.class, "lift");

        //Set the motors to travel based on a position
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reverse the Right Motors
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        //Instantiates a Drive Train with the motors set to the correct mode for autonomous
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
        driveTrain.setTelemetry(this.telemetry);
        driveTrain.setIMU(imu);
        intake = new Intake(lift, null, null, null);

        //Instantiates a Camera Object for use with Mineral Detection
        cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        //Set the encoder position back to 0
        rearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Tell the motors to go back to position based travel (Might be unnecessary based on type of motor used)
        rearLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rearRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    //Loop required for all Autonomous modes
    @Override
    public abstract void runOpMode() throws InterruptedException;

    public void initInit() {
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    //Descend robot from Lander, move off the hook
    protected void lowerBot() {
        //intake.liftPosition(0.005);//1.130588671

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        runtime.reset();
        lift.setTargetPosition(2318);
        lift.setPower(.5);
        while(lift.isBusy() && runtime.seconds() < 2) {}

        lift.setPower(0);

        //driveTrain.rotateDegrees(270);

    }

    //Locates the gold mineral from one of the three given locations
    protected void prospect(){
        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();

        double time = runtime.milliseconds();
        //while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL) {

        while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL && runtime.milliseconds() < time + 12000) {
            cam.betterUpdate(telemetry);
            telemetry.update();
            if(runtime.milliseconds() < time + 4000 && !rotatedLeft) {
                driveTrain.rotateDegrees(-5);
                rotatedLeft = true;
            }
            if(runtime.milliseconds() < time + 8000 & !rotatedRight) {
                driveTrain.rotateDegrees(10);
                rotatedRight = true;
            }

        }
        cam.stopTFOD();


        //driveTrain.rotateDegrees((camTries - 1) / 4f);

    }

    protected void playSurprise() {
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, surprise);
    }
}
