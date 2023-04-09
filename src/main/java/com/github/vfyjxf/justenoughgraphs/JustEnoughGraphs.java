package com.github.vfyjxf.justenoughgraphs;

import com.github.vfyjxf.justenoughgraphs.api.Globals;
import com.github.vfyjxf.justenoughgraphs.setup.ClientSetuper;
import com.github.vfyjxf.justenoughgraphs.setup.CommonSetuper;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Globals.MOD_ID)
public class JustEnoughGraphs {

    public static final Logger LOGGER = LogManager.getLogger("JustEnoughGraphs");

    public JustEnoughGraphs() {
        DistExecutor.unsafeRunForDist(() -> ClientSetuper::new, () -> CommonSetuper::new);
    }


}
