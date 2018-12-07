package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Robot.BuggleCam;
import org.firstinspires.ftc.teamcode.Robot.DriveTrain;
import org.firstinspires.ftc.teamcode.Robot.Intake;

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
        driveTrain = new DriveTrain(
                hardwareMap.get(DcMotorEx.class, "backLeft"), hardwareMap.get(DcMotorEx.class, "backRight"),
                hardwareMap.get(DcMotorEx.class, "frontLeft"), hardwareMap.get(DcMotorEx.class, "frontRight"));
        intake = new Intake(hardwareMap.get(DcMotorEx.class, "lift"), null, null,
                hardwareMap.get(DcMotorEx.class, "intakeLift"));
        intake.setTelemetry(this.telemetry);
        //Reverse the Right Motors
        driveTrain.reverseMotors();


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        //Instantiates a Drive Train with the motors set to the correct mode for autonomous
        driveTrain.setTelemetry(this.telemetry);
        driveTrain.setIMU(imu);

        //Instantiates a Camera Object for use with Mineral Detection
        cam = new BuggleCam(telemetry, hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName()));

        //Set the encoder position back to 0
        driveTrain.resetEncoders();
        intake.resetEncoders();
        intake.lockIntake();
    }

    //Loop required for all Autonomous modes
    @Override
    public abstract void runOpMode();

    //Descend robot from Lander, move off the hook
    protected void lowerBot() {
        intake.resetEncoders();
        runtime.reset();
        intake.liftPosition(-3.3);
        while(runtime.seconds() < 4 && opModeIsActive()) {}
        intake.resetEncoders();
        intake.lift(0);
    }

    protected void lowerBot(double power) {
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setPower(power);
        runtime.reset();
        while(lift.isBusy() && runtime.seconds() < 4 && opModeIsActive()) {}
        lift.setPower(0);
    }

    //Locates the gold mineral from one of the three given locations
    protected void prospect(){
        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();

        runtime.reset();
        driveTrain.resetHeading();
        rotatedLeft = false;
        rotatedRight = false;

        while(cam.getGoldPosition() == BuggleCam.GOLD_POSITION.NULL && runtime.seconds() < 8 && opModeIsActive()) {
            cam.betterUpdate(telemetry);
            telemetry.update();
            if(runtime.seconds() > 3 && !rotatedLeft) {
                driveTrain.rotateDegrees(-10);
                rotatedLeft = true;
            }
            if(runtime.seconds() > 6 & !rotatedRight && cam.getGoldPosition() != BuggleCam.GOLD_POSITION.NULL) {
                driveTrain.rotateDegrees(20);
                rotatedRight = true;
            }

        }
        cam.stopTFOD();

    }

    protected void dispenseMarker() {
        runtime.reset();
        while(runtime.seconds() < 3) {}
    }

    protected void playSurprise() {
        SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, surprise);
    }
}
