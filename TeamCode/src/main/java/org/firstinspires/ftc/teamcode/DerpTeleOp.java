package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.Robot.DriveTrain;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import com.qualcomm.robotcore.util.ElapsedTime;

@SuppressWarnings("Duplicate")
@TeleOp(name = "TeleOp", group = "TeleOp")
public class DerpTeleOp extends OpMode {

    private boolean isSurprising, aDepressed, xDepressed, rightBumper, leftBumper;
    private double targetXPower, targetYPower;
    private DcMotorEx rearLeft, rearRight, frontLeft, frontRight;
    private DcMotorEx lift, intakeLift, intakeSpool, intake;
    private DriveTrain driveTrain;
    private Intake intakeMotors;
    private ElapsedTime timer;

    @Override
    public void init() {
        targetXPower = 0;
        targetYPower = 0;
        timer = new ElapsedTime();
        //Initalize each motor from the Hardware Map on the phone
        rearLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        rearRight = hardwareMap.get(DcMotorEx.class, "backRight");
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");


        lift = hardwareMap.get(DcMotorEx.class, "lift");
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
        intakeMotors = new Intake(
                lift,
                hardwareMap.get(DcMotorEx.class, "slide"),
                hardwareMap.get(DcMotorEx.class, "depositor"),
                hardwareMap.get(DcMotorEx.class, "intakeLift"),
                hardwareMap.servo.get("basket"),
                hardwareMap.get(CRServo.class, "intake"),
                hardwareMap.servo.get("trapdoor"),
                hardwareMap.servo.get("phoneMount"));

        telemetry.addData("Slide position", intakeMotors.getSlideEncoderPosition());

    }

    @Override
    public void loop() {
        telemetry.addData("Depositor position", intakeMotors.getDepositorPosition());
        telemetry.update();
        controlIntake();
        controlLift();
        mecanumTrain();
        controlBasket();
        sendTelemetry();
    }

    public void mecanumTrain() {
        driveTrain.newOmni(-gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_bumper);
    }

    /*
     * Moves the Robot in the four cardinal directions depending on which axis is being pushed furthest on the right stick
     * Goes forward, backward, left, or right.
     */
    public void fourDirectionalMovement() {
        targetXPower = -gamepad1.left_stick_x;
        targetYPower = -gamepad1.left_stick_y;

        if(!gamepad1.right_bumper){
            targetYPower *= .3;
            targetXPower *= .5;
        }

        if(Math.abs(gamepad1.left_stick_x) > Math.abs(gamepad1.left_stick_y)) {
            driveTrain.rotate((float) -targetXPower * .75f);
        } else {
            if (targetYPower < 0) {
                driveTrain.longitudinal(targetYPower);
            } else if (targetYPower > 0) {
                driveTrain.combinedDirections(targetXPower * 0.75f, targetYPower);
            } else if (targetYPower == 0) {
                driveTrain.rotate((float) (targetXPower * .75f));
            }
        }
    }

    public void controlIntake() {
        if(gamepad2.left_bumper)
            intakeMotors.outtake(1);
        else if (gamepad2.right_bumper)
            intakeMotors.intake(1);
        if(!gamepad2.left_bumper && !gamepad2.right_bumper)
            intakeMotors.intake(0);

        if(!gamepad2.a && aDepressed)
            intakeMotors.toggleTrapDoor();

        if(gamepad2.dpad_up)
            intakeMotors.moveDepositor(.3);
        else if(gamepad2.dpad_down)
            intakeMotors.moveDepositor(-.15);
        else
            intakeMotors.moveDepositor(0);


        if(gamepad2.x || gamepad2.b) {
            telemetry.addData("Slide Position", intakeMotors.moveSlide(gamepad2.x));
            telemetry.addData("Slide Target Position", intakeMotors.baseSlidePosition);
            if (gamepad2.b && intakeMotors.moveSlide(gamepad2.x))
            {
                intakeMotors.intakeBasket(true);
                intakeMotors.intakeBasket(false);
                intakeMotors.controlBasket(0);
            }
        }

        intakeMotors.moveSlide(-gamepad2.left_stick_y * .5);

        if (!gamepad2.y)
            intakeMotors.intakeBasket(gamepad2.right_stick_y * .25); //manual control
        else
            intakeMotors.intakeBasket(.1625);

        telemetry.addData("Intake Position", intakeMotors.getIntakePosition());

        //intakeMotors.intakeBasket(); //automatic control

        aDepressed = gamepad2.a;
        xDepressed = gamepad2.x;
        rightBumper = gamepad2.right_bumper;
        leftBumper = gamepad2.left_bumper;

    }

    //Up on left stick to raise lift, down on left stick to retract lift, scales with force on stick
    public void controlLift() {
        if(gamepad1.dpad_up)
            intakeMotors.lift(1);
        else if(gamepad1.dpad_down)
            intakeMotors.lift(-1);
        else
            intakeMotors.lift(0);
    }

    public void controlBasket() {
        if(gamepad2.dpad_left)
            intakeMotors.controlBasket(1);
        else if(gamepad2.dpad_right)
            intakeMotors.controlBasket(0);
        //
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
