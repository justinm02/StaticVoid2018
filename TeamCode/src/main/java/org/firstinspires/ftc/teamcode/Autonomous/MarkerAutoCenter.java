package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Autonomous.AutoOp;

//@Autonomous(name = "MarkerCenter", group = "Autonomous")
public class MarkerAutoCenter extends AutoOp {

    @Override
    public void runOpMode() {
        initialize();
        while(!isStarted()) {
            intake.lift(-.075);
        }
        waitForStart();
        //playSurprise();

        //Descend robot from Lander, move off the hook
        lowerBot();

        //Hits center mineral
        longitudinalDistance(-18, 0.3);

    }
}
