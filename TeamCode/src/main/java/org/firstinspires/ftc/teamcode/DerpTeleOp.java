package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.Robot.DriveTrain;
import org.firstinspires.ftc.teamcode.Robot.Intake;

@SuppressWarnings("Duplicate")
@TeleOp(name = "TeleOp", group = "TeleOp")
public class DerpTeleOp extends OpMode {

    private boolean isSurprising;
    private double targetXPower, targetYPower;
    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift, intakeLift, intakeSpool, intake;
    private DriveTrain driveTrain;
    private Intake intakeMotors;

    @Override
    public void init() {
        targetXPower = 0;
        targetYPower = 0;

        //Initalize each motor from the Hardware Map on the phone
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");


        lift = hardwareMap.get(DcMotorEx.class, "lift");
        intake = hardwareMap.get(DcMotorEx.class, "noodles");
        /*
        intakeLift = hardwareMap.get(DcMotorEx.class, "intakeLift");
        intakeSpool = hardwareMap.get(DcMotorEx.class, "intakeSpool");
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        */

        //Set each motor to run using encoders
        rearLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        /*
        intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeSpool.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        */

        //Set the motors to brake when no power is given
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);



        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        /*
        intakeLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        */


        //Reverse the right motors
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);


        isSurprising = false;


        //Instantiate Drive Train class with instantiated motors
        driveTrain = new DriveTrain(rearLeft, rearRight, frontLeft, frontRight);
        intakeMotors = new Intake(lift, intake, hardwareMap.get(DcMotorEx.class, "slide"),
                hardwareMap.get(DcMotorEx.class, "intakeLift"), hardwareMap.servo.get("basket"),
                hardwareMap.get(CRServo.class, "intake"));
        //intakeMotors = new Intake(lift, intake, intakeSpool, intakeLift);
    }

    @Override
    public void loop() {

        controlIntake();
        controlLift();
        fourDirectionalMovement();
        controlBasket();
        sendTelemetry();

    }

    /*
     * Moves the Robot in the four cardinal directions depending on which axis is being pushed furthest on the right stick
     * Goes forward, backward, left, or right.
     */
    public void fourDirectionalMovement() {
        targetXPower = -gamepad1.left_stick_x;
        targetYPower = -gamepad1.left_stick_y;

        if(gamepad1.right_trigger != 0 || gamepad1.right_bumper){
            targetYPower *= .3;
            targetXPower *= .25;
        }

        if(Math.abs(targetXPower) > Math.abs(targetYPower)) {
            driveTrain.rotate((float) -targetXPower * .5f);
        } else {
            if (targetYPower < 0) {
                driveTrain.longitudinal(targetYPower);
            } else if (targetYPower > 0) {
                driveTrain.combinedDirections(targetXPower * 0.5f, targetYPower);
            } else if (targetYPower == 0) {
                driveTrain.rotate((float) (targetXPower * .5f));
            } else if (targetXPower == 0 && targetYPower == 0) {
                driveTrain.rotate(-gamepad1.right_stick_x * .5f);
            }
        }
    }

    public void controlIntake() {
        if(gamepad2.a) {
            if (gamepad2.left_bumper)
                intakeMotors.outtake(.5);
            else if(gamepad2.right_bumper)
                intakeMotors.intake(.5);
        } else {
            if(gamepad2.left_bumper)
                intakeMotors.outtake(1);
            else if (gamepad2.right_bumper)
                intakeMotors.intake(1);
        }
        if(!gamepad2.left_bumper && !gamepad2.right_bumper)
            intakeMotors.intake(0);

        intakeMotors.moveSlide(-gamepad2.right_stick_y * .2);
        if(gamepad2.dpad_up)
            intakeMotors.moveIntake(.4);
        else if(gamepad2.dpad_down)
            intakeMotors.moveIntake(-.4);
        else {
            intakeMotors.moveIntake(0);
        }
    }

    //Up on left stick to raise lift, down on left stick to retract lift, scales with force on stick
    public void controlLift() {
        if(gamepad2.left_stick_y <= 0)
            telemetry.addData("Lift Position",intakeMotors.lift(gamepad2.left_stick_y * 0.40));
        else if (gamepad2.left_stick_y > 0)
            telemetry.addData("Lift Position", intakeMotors.lift(gamepad2.left_stick_y * 0.5));
    }

    public void controlBasket() {
        if(gamepad2.dpad_left)
            intakeMotors.controlBasket(1, gamepad2.right_stick_y * .5f);
        else if(gamepad2.dpad_right)
            intakeMotors.controlBasket(0, gamepad2.right_stick_y * .5f);
        else
            intakeMotors.controlBasket(gamepad2.right_stick_y * .5f);
    }

    public void sendTelemetry() {
        telemetry.addData("Gamepad1 Left Stick X", gamepad1.left_stick_x);
        telemetry.addData("Gamepad1 Left Stick Y", gamepad1.left_stick_y);
        telemetry.addData("Front Motors", "Left: (%.2f) | Right: (%.2f)", frontLeft.getPower(), frontRight.getPower());
        telemetry.addData("Rear Motors", "Left: (%.2f) | Right: (%.2f)", rearLeft.getPower(), rearRight.getPower());
        telemetry.addData("Target Front Pos", "Left: (%d) | Right: (%d)", frontLeft.getTargetPosition(), frontRight.getTargetPosition());
        telemetry.addData("A Button: ", gamepad2.a);
        telemetry.addData("GamePad2 Right Stick Y", -gamepad2.right_stick_y);
        telemetry.update();
    }
}
