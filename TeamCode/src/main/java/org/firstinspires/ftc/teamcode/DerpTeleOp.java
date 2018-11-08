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

    private boolean isSurprising, eightDirectional, intaked;
    private PIDControl driveTrainPID;
    private double targetXPower, targetYPower, averagePower, targetRotatePower;
    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift, intakeLift, intakeSpool, intake;
    private DriveTrain driveTrain;
    private Intake intakeMotors;
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

        lift = hardwareMap.get(DcMotorEx.class, "lift");
        intakeLift = hardwareMap.get(DcMotorEx.class, "intakeLift");
        intakeSpool = hardwareMap.get(DcMotorEx.class, "intakeSpool");
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        //Set each motor to run using encoders
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeSpool.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Set the motors to brake when no power is given
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intakeLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Reverse the right motors
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);


        isSurprising = false;
        intaked = false;

        //Instantiate Drive Train class with instantiated motors
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
        intakeMotors = new Intake(lift, intake, intakeSpool, intakeLift);
    }

    @Override
    public void loop() {
        sendTelemetry();

        controlIntake();
        controlLift();

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

    public void controlIntake() {
        if(gamepad2.a) {
            if (gamepad2.left_bumper)
                intakeMotors.outtake(.3);
            else if(gamepad2.right_bumper)
                intakeMotors.intake(.3);
        } else {
            if(gamepad2.left_bumper)
                intakeMotors.outtake(.6);
            else if (gamepad2.right_bumper)
                intakeMotors.intake(.6);
        }
        if(!gamepad2.left_bumper && !gamepad2.right_bumper)
            intakeMotors.intake(0);

        intakeMotors.moveSlide(gamepad2.right_stick_y/2);

        if(gamepad2.right_stick_x == 0)
            intakeMotors.moveIntake(0);
        else
            intakeMotors.moveIntake(gamepad2.right_stick_x/6);
    }

    public void controlLift() {
        if(gamepad2.left_stick_y >= 0)
            intakeMotors.lift(gamepad2.left_stick_y/4);
        else if (gamepad2.left_stick_y < 0)
            intakeMotors.lift(gamepad2.left_stick_y/8);
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
