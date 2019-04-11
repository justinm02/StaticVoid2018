package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.Robot.DriveTrain;
import org.firstinspires.ftc.teamcode.Robot.Intake;

@SuppressWarnings("Duplicate")
@TeleOp(name = "TeleOp", group = "TeleOp")
public class DerpTeleOp extends OpMode {

    private boolean trapDoorMoved = false;
    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DriveTrain driveTrain;
    private Intake intakeMotors;

    @Override
    public void init() {
        //Initalize each motor from the Hardware Map on the phone
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");


        DcMotorEx lift = hardwareMap.get(DcMotorEx.class, "lift");

        //Set each motor to run using encoders
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //Set the motors to brake when no power is given
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        //Reverse the right motors
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);


        //Instantiate Drive Train class with instantiated motors
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
        intakeMotors = new Intake(
                lift,
                hardwareMap.get(DcMotorEx.class, "depositorSlide"),
                hardwareMap.get(DcMotorEx.class, "intakeSlide"),
                hardwareMap.get(DcMotorEx.class, "intakeLift"),
                hardwareMap.servo.get("basket"),
                hardwareMap.get(CRServo.class, "intake"),
                hardwareMap.servo.get("trapdoor"),
                hardwareMap.servo.get("phoneMount"));

        //instantiate trapdoor
        intakeMotors.toggleTrapDoor(true);

        telemetry.addData("Slide position", intakeMotors.getDepositorSlideEncoderPosition());
    }

    @Override
    public void loop() {
        telemetry.addData("Depositor Slide Position", intakeMotors.getDepositorSlideEncoderPosition());
        telemetry.addData("Intake Slide Position", intakeMotors.getIntakeSlideEncoderPosition());
        telemetry.update();
        controlIntake();
        controlLift();
        mecanumTrain();
        //controlMarkerDepositor();
        sendTelemetry();
    }

    public void mecanumTrain() {
        if(gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0) {
            double right = gamepad1.dpad_right ? .7 : 0;
            double left = gamepad1.dpad_left ? .7 : 0;
            //double forward = gamepad1.dpad_up ? .7 : 0;
            //double backward = gamepad1.dpad_down ? .7 : 0;
            driveTrain.newOmni(left - right,  0, 0, gamepad1.right_bumper);
        } else {
            if(gamepad1.left_bumper)
                driveTrain.newOmni(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x * .8f, gamepad1.right_bumper);
            else
                driveTrain.newOmni(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x * .8f, gamepad1.right_bumper);
        }
    }

    public void controlIntake() {
        if(gamepad2.left_trigger > 0)
            intakeMotors.outtake(1);
        else if (gamepad2.right_trigger > 0)
            intakeMotors.intake(1);
        if(!(gamepad2.left_trigger > 0) && !(gamepad2.right_trigger > 0))
            intakeMotors.intake(0);

        if(gamepad2.b && !trapDoorMoved && !gamepad2.start) {
            intakeMotors.toggleTrapDoor(false);
            trapDoorMoved = true;
        } else if (trapDoorMoved && (!gamepad2.b))
            trapDoorMoved = false;


        //move depositor slide
        if(gamepad2.right_bumper) {
            telemetry.addData("Depositor Slide Position", intakeMotors.moveDepositorSlide("up"));
            telemetry.addData("Depositor Slide Target Position", intakeMotors.baseDepositorSlidePosition);
            intakeMotors.setTrapDoorPosition(0);
        }
        else if (gamepad1.right_bumper) {
            telemetry.addData("Depositor Slide Position", intakeMotors.moveDepositorSlide("down"));
            telemetry.addData("Depositor Slide Target Position", intakeMotors.baseDepositorSlidePosition);
            intakeMotors.setTrapDoorPosition(0);
        }
        /*else
            intakeMotors.moveDepositorSlide(gamepad2.left_stick_y * .5);*/

        //move intake slide
        if(gamepad2.right_stick_y < 0) {
            telemetry.addData("Intake Slide Position", intakeMotors.moveIntakeSlide("up"));
            //telemetry.addData("Depositor Slide Target Position", intakeMotors.baseSlidePosition);
        }
        else if (gamepad2.right_stick_y > 0) {
            telemetry.addData("Intake Slide Position", intakeMotors.moveIntakeSlide("down"));
            //telemetry.addData("Depositor Slide Target Position", intakeMotors.baseSlidePosition);
        }
        else {
            intakeMotors.moveIntakeSlide("neutral");
        }



        //move basket up

        intakeMotors.intakeBasket(gamepad2.left_stick_y * .75); //manual control

        telemetry.addData("Intake Position", intakeMotors.getIntakePosition());

    }

    //Up on left stick to raise lift, down on left stick to retract lift, scales with force on stick
    public void controlLift() {
        if(gamepad1.y)
            intakeMotors.lift(1);
        else if(gamepad1.a)
            intakeMotors.lift(-1);
        else
            intakeMotors.lift(0);
    }

    public void controlMarkerDepositor() {
        if(gamepad2.dpad_left)
            intakeMotors.markerDepositor(1);
        else if(gamepad2.dpad_right)
            intakeMotors.markerDepositor(0);
    }

    public void sendTelemetry() {
        telemetry.addData("Gamepad1 Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Gamepad1 Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Front Motors", "Left: (%.2f) | Right: (%.2f)", frontLeft.getPower(), frontRight.getPower());
        telemetry.addData("Rear Motors", "Left: (%.2f) | Right: (%.2f)", rearLeft.getPower(), rearRight.getPower());
        telemetry.addData("Target Front Pos", "Left: (%d) | Right: (%d)", frontLeft.getTargetPosition(), frontRight.getTargetPosition());
        telemetry.addData("A Button: ", gamepad2.a);
        telemetry.addData("GamePad2 Right Stick Y", -gamepad2.right_stick_y);
        //telemetry.addData("Depositor position", intakeMotors.getDepositorPosition());
        telemetry.update();
    }
}
