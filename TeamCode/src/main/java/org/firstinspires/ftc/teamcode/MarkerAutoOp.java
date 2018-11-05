package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "MarkerOp", group = "Autonomous")
public class MarkerAutoOp extends AutoOp {


    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        waitForStart();
        cam.update();
    }
}
