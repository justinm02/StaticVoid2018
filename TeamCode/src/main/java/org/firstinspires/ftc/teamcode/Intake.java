package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Intake {

    private DcMotorEx lift, noodles, slide;

    public Intake (DcMotorEx lift, DcMotorEx noodles, DcMotorEx slide) {
        this.lift = lift;
        this.noodles = noodles;
        this.slide = slide;
    }

    public void lift(double power) {
        lift.setPower(power);
    }
}
