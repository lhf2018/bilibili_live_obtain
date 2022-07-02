package lhf2018.message.handler;

public enum HandlerType {
    DANMU_MSG {
        @Override
        public Class getHandler() {
            return DanMuHandler.class;
        }
    };

    public abstract Class getHandler();
}
