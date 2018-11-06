package dang.note.spring.common.entiy.constant;

import java.io.Serializable;

public enum Code implements Serializable {

    SUCCESS(200, "操作成功"),
    CONFIRM(201, ""),
    ERROR(500, "操作失败"),
    NULL_CODE(4444, "null");


    private final int code;
    private final String message;

    private Code(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public static Code getByCode(int code) {
        for (Code e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        return NULL_CODE;
    }

}
