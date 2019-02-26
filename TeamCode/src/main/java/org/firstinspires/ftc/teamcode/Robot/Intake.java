package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;

public class Intake{

    private DcMotorEx lift, slide, depositor, intakeLift;
    private Servo basket, trapdoor;
    private CRServo intake;
    private Telemetry telemetry;
    public ElapsedTime runtime;

    private IntakePosition intakePosition;
    private SlidePosition slidePosition;

    private int baseDepositorPosition;
    public int baseSlidePosition;
    private static final double COUNTS_PER_REVOLUTION = 1680;

    public Intake (DcMotorEx lift, DcMotorEx slide, DcMotorEx depositor, DcMotorEx intakeLift, Servo basket, CRServo intake, Servo trapdoor) {
        this.lift = lift;
        this.slide = slide;
        this.depositor = depositor;
        this.intakeLift = intakeLift;
        this.basket = basket;
        this.trapdoor = trapdoor;
        runtime = new ElapsedTime();
        if(depositor != null) {
            depositor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            depositor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(basket != null) {
            basket.setDirection(Servo.Direction.FORWARD);
            basket.setPosition(1);
        }
        this.intake = intake;
        if(slide != null) {
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            baseSlidePosition = slide.getCurrentPosition();
        }
        intakePosition = IntakePosition.UP;
        slidePosition = SlidePosition.IN;
        baseDepositorPosition = intakeLift.getCurrentPosition();
    }

    public enum IntakePosition {
        UP,
        HORIZONTAL,
        DOWN
    }

    public enum SlidePosition {
        IN,
        OUT
    }

    public IntakePosition getIntakePosition() {
        return intakePosition;
    }

    public void setIntakePosition (IntakePosition position) {
        intakePosition = position;
    }

    public SlidePosition getSlidePosition() {
        return slidePosition;
    }

    public int getSlideEncoderPosition() { return slide.getCurrentPosition(); }

    public int getDepositorPosition() { return intakeLift.getCurrentPosition(); }

    public void setSlidePosition(SlidePosition position) {
        this.slidePosition = position;
    }

    public void setTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public double lift(double power) {
        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lift.setPower(power);
        return lift.getCurrentPosition();
    }

    public void resetEncoders() {
        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void liftPosition(double ticks) {
        resetEncoders();
        lift.setTargetPosition((int) (ticks));
        lift.setPower(1);
        runtime.reset();
        while(lift.isBusy() && runtime.seconds() < 5) {
            //telemetry.addData("Status", "Lifting");
            //telemetry.addData("Desired Position", ticks);
            //telemetry.addData("Current Position", lift.getCurrentPosition());
            //telemetry.update();
        }
        lift.setPower(0);

    }

    public void intakeBasket(boolean transfer /*Refers to transfer of minerals to depositing bucker*/) {
        /*if (rightBumper) { //down
            intakeLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeLift.setTargetPosition(330);

            intakeLift.setPower(.7);
            //while (intakeLift.isBusy()) {
            telemetry.addData("Motor Position", intakeLift.getCurrentPosition());
            telemetry.addData("Is Busy", intakeLift.isBusy());
            telemetry.update();
            //}
            //intakeLift.setPower(0);
        }
        else if (leftBumper) { //halfway
            intakeLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            intakeLift.setTargetPosition(150);

            intakeLift.setPower(.1);
            telemetry.addData("Motor Position", intakeLift.getCurrentPosition());
            telemetry.addData("Is Busy", intakeLift.isBusy());
            telemetry.update();
        }
        else {
            intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            intakeLift.setPower(0);
        }*/


        //intakeLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        if(intakeLift.getMode() != DcMotor.RunMode.RUN_TO_POSITION)
            intakeLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (transfer) {
            intakeLift.setTargetPosition(baseDepositorPosition);
            intakeLift.setPower(.5);
        }

        else {
            intakeLift.setTargetPosition(baseDepositorPosition + 450);
            intakeLift.setPower(.15);
        }

        intakeLift.setPower(0);

        /*switch(intakePosition) {
            case UP:
                intakeLift.setTargetPosition(baseDepositorPosition);
                intakeLift.setPower(.5);
                break;
            case HORIZONTAL:
                intakeLift.setTargetPosition(baseDepositorPosition - 150);
                intakeLift.setPower(.15);
                break;
            case DOWN:
            default:
                intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                intakeLift.setPower(0);
                break;*/
    }

    public void intakeBasket(double power) {
        if(intakeLift.getMode() != DcMotor.RunMode.RUN_USING_ENCODER)
            intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeLift.setPower(.5 * power);
    }

    public void lockIntake() {
        depositor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        depositor.setPower(0);
    }

    public void outtake(double power) {
        intake.setPower(-power);
    }

    public void intake(double power) {
        intake.setPower(power);
    }

    public boolean moveSlide(boolean full) {
        //slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        if (full)
            slide.setTargetPosition(baseSlidePosition + 5366);
        else
            slide.setTargetPosition(baseSlidePosition + 4100);
        slide.setPower(.5);

        return true; //method finished
    }

    public void resetSlidePosition() {
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public double moveSlide(double power) {
        if(slide.getMode() != DcMotor.RunMode.RUN_USING_ENCODER)
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slide.setPower(power);
        return slide.getCurrentPosition();
    }

    public void moveDepositor(double power) {
        depositor.setPower(power);
    }

    public void controlBasket(double servo) {
        basket.setPosition(servo);
    }

    public void toggleTrapDoor() {
        trapdoor.setPosition(1 - trapdoor.getPosition());
    }

}
