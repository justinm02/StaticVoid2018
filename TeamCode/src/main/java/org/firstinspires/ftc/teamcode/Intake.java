package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {

    private DcMotorEx lift, noodles, slide, intakeLift;

    public Intake (DcMotorEx lift, DcMotorEx noodles, DcMotorEx slide, DcMotorEx intakeLift) {
        this.lift = lift;
        this.noodles = noodles;
        this.slide = slide;
        this.intakeLift = intakeLift;
    }

    public void lift(double power) {
        lift.setPower(power);
    }

    public void liftPosition(int position) {
        lift.setTargetPosition(position);
        lift.setPower(.1);
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
