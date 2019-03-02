package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name="ProgressiveOp", group = "Autonomous")
public class ProgressiveOp extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();
        waitForStart();
        //prospect();
        //resetHeading();
        longitudinalDistance(100, 0.2f);
        park();
    }
}