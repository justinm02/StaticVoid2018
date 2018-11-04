package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;


@TeleOp(name = "TeleOp", group = "TeleOp")
public class DerpTeleOp extends OpMode {

    private boolean isSurprising, eightDirectional;
    private PIDControl driveTrainPID;
    private double targetXPower, targetYPower, averagePower, targetRotatePower;
    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift;
    private DriveTrain driveTrain;
    private GyroSensor gyroSensor;

    @Override
    public void init() {
        driveTrainPID = new PIDControl(0,0,0);
        targetXPower = 0;
        targetYPower = 0;
        averagePower = 0;

        //Initalize each motor from the Hardware Map on the phone
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");

        //lift = hardwareMap.get(DcMotorEx.class, "lift");

        //Set each motor to run using encoders
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Set the motors to brake when no power is given
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reverse the right motors
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);


        isSurprising = false;

        //Instantiate Drive Train class with instantiated motors
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
    }

    @Override
    public void loop() {
        sendTelemetry();

        fourDirectionalMovement();
        checkForSurprise();
    }


    /*
     * Moves the Robot in only the four cardinal directions, depending on which controller axis is being pushed the furthest.
     * Goes forward, backward, left, or right.
     */
    public void fourDirectionalMovement() {
        targetXPower = gamepad1.left_stick_x;
        targetYPower = -gamepad1.left_stick_y;

        //Check if the left stick is being pressed
        if(targetXPower != 0 || targetYPower != 0) {
            //Check which direction the right stick is being pressed more; horizontally or vertically
            if (Math.abs(targetXPower) > Math.abs(targetYPower)) {
                driveTrain.lateral(targetXPower);
            } else {
                driveTrain.longitudinal(targetYPower);
            }
            //If Right Stick isn't being pressed, go on the check for rotation
        } else {
            driveTrain.rotate(gamepad1.right_stick_x);
        }
    }


    public void checkForSurprise() {
        if(gamepad1.start && gamepad1.left_stick_button && !isSurprising) {
            //FtcRobotControllerActivity.surprise.start();
            isSurprising = true;
        } else if(gamepad1.back && gamepad1.left_stick_button && isSurprising) {
            //FtcRobotControllerActivity.surprise.stop();
            isSurprising = false;
        }
    }

    public void sendTelemetry() {
        telemetry.addData("Eight Directional Movement" , eightDirectional);
        telemetry.addData("Gamepad1 Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Gamepad1 Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Front Motors", "Left: (%.2f) | Right: (%.2f)", frontLeft.getPower(), frontRight.getPower());
        telemetry.addData("Rear Motors", "Left: (%.2f) | Right: (%.2f)", rearLeft.getPower(), rearRight.getPower());
        telemetry.addData("Target Front Pos", "Left: (%d) | Right: (%d)", frontLeft.getTargetPosition(), frontRight.getTargetPosition());
        telemetry.update();
    }
}
