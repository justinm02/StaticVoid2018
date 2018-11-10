package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake{

    private DcMotorEx lift, noodles, slide, intakeLift;

    private static final double COUNTS_PER_REVOLUTION = 1680;

    public Intake (DcMotorEx lift, DcMotorEx noodles, DcMotorEx slide, DcMotorEx intakeLift) {
        this.lift = lift;
        this.noodles = noodles;
        this.slide = slide;
        this.intakeLift = intakeLift;
    }

    public void lift(double power) {
        lift.setPower(power);
    }

    public void liftPosition(double revolutions) {
        lift.setTargetPosition((int) (revolutions * COUNTS_PER_REVOLUTION));
        lift.setPower(.1);
        while(lift.isBusy()) {}
    }

    public void outtake(double power) {
        noodles.setPower(-power);
    }

    public void intake(double power) {
        noodles.setPower(power);
    }

    public void moveSlide(double power) {
        slide.setPower(power);
    }

    public void moveIntake(double power) {
        intakeLift.setPower(power);
    }
}
