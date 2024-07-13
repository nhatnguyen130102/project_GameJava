package objects;

import static ultilz.Constants.ObjectConstants.*;

public class PalmTree extends GameObject{
    public PalmTree(int x, int y, int objType) {
        super(x, y, objType);
        createHitBox();
    }

    private void createHitBox() {
        initHitBox(HB_PALM_TREE_WIDTH,HB_PALM_TREE_HEIGHT);
    }

    public void update(){
        if(doAnimation)
            updateFrameTick();
    }
}
