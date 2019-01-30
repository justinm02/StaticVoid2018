package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;

public class Intake{

    private DcMotorEx lift, noodles, slide, intakeLift;
    private Servo basket;
    private CRServo intake;
    private Telemetry telemetry;
    public ElapsedTime runtime;

    private static final double COUNTS_PER_REVOLUTION = 1680;

    public Intake (DcMotorEx lift, DcMotorEx noodles, DcMotorEx slide, DcMotorEx intakeLift, Servo basket, CRServo intake) {
        this.lift = lift;
        this.noodles = noodles;
        this.slide = slide;
        this.intakeLift = intakeLift;
        this.basket = basket;
        runtime = new ElapsedTime();
        if(intakeLift != null) {
            intakeLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        if(basket != null)
            basket.setDirection(Servo.Direction.FORWARD);
        this.intake = intake;
        if(slide != null)
            slide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

    public void liftPosition(double revolutions) {

        lift.setTargetPosition((int) (revolutions * COUNTS_PER_REVOLUTION));
        lift.setPower(.45);
        runtime.reset();
        while(lift.isBusy() && runtime.seconds() < 5) {
            telemetry.addData("Status", "Lifting");
            telemetry.addData("Desired Position", revolutions * COUNTS_PER_REVOLUTION);
            telemetry.addData("Current Position", lift.getCurrentPosition());
            telemetry.update();
        }
        lift.setPower(0);

    }

    public void lockIntake() {
        intakeLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeLift.setPower(0);
    }

    public void outtake(double power) {
        intake.setPower(-power);
    }

    public void intake(double power) {
        intake.setPower(power);
    }

    public double moveSlide(double power) {
        slide.setPower(power);
        return slide.getPower();
    }

    public void moveIntake(double power) {
        intakeLift.setPower(power);
    }

    public void controlBasket(float servo, float yPower) {
        basket.setPosition(servo);
        //slide.setPower(yPower);
    }

    public void controlBasket(float yPower) {
        //slide.setPower(yPower);
    }

}
