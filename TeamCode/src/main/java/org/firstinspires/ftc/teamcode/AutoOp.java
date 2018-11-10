package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public abstract class AutoOp extends LinearOpMode {

    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift;


    protected DriveTrain driveTrain;
    protected Intake intake;
    protected BuggleCam cam;

    //Order of Operations:
    //
    //1) Lower Robot
    //2) Place Marker
    //3) Knock gold square
    //4) Lean on Crater

    public void initialize() {

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

        //Instantiates a Drive Train with the motors set to the correct mode for autonomous
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
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

    //Descend robot from Lander, move off the hook
    protected void lowerBot() {
        //intake.liftPosition(-0.2);//1.130588671
        driveTrain.lateralDistance(2);
        driveTrain.longitudinalDistance(-6);
        driveTrain.lateralDistance(-2);
        driveTrain.rotateDegrees(170);

    }

    //Locates the gold mineral from one of the three given locations
    protected void findGold(){
        //While the Gold Position is undetermined, keep updating the camera
        cam.activateTFOD();

        int camTries = 0;
        while(cam.getGoldPosition() == null) {
            cam.update();
            camTries++;
            if(camTries % 4 == 1 && camTries <= 81)
                driveTrain.rotateDegrees(1, 0.1);
        }
        cam.stopTFOD();

        driveTrain.rotateDegrees((camTries - 1) / 4f);
    }
}
