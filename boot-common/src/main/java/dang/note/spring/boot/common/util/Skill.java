package dang.note.spring.boot.common.util;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Description: 技能 java 语法糖
 */
public class Skill {

    /**
     * 判断对象链是否为空（a.b.c==null  a为空  则返回true 而不是报空指针）
     *
     * @param supplier
     * @return
     */
    public static boolean isNull(Supplier supplier) {
        try {
            Object data = supplier.get();
            if (data == null) {
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    /**
     * 判断对象是否为空
     *
     * @param supplier
     * @return
     */
    public static boolean isEmpty(Supplier supplier) {
        if (isNull(supplier)) {
            return true;
        }
        return isEmpty(supplier.get());
    }

    public static boolean notEmpty(Supplier supplier) {
        return !isEmpty(supplier);
    }

    public static boolean notEmpty(Object data) {
        return !isEmpty(data);
    }

    public static boolean notEmptyDo(Object data, Consumer consumer) {
        if (notEmpty(data)) {
            consumer.accept(data);
            return true;
        }
        return false;
    }

    /**
     * 判断数据是否为空
     *
     * @return 是否为空
     * @data 数据
     */
    public static boolean isEmpty(Object data) {
        if (data == null) {
            return true;
        }
        if (data instanceof String) {   // String
            String str = (String) data;
            if (str.length() == 0) {
                return true;
            }
            return false;
        } else if (data instanceof Collection) {    //List  set
            Collection coll = (Collection) data;
            if (coll.size() == 0) {
                return true;
            }
            return false;
        } else if (data instanceof Map) {   // Map
            Map map = (Map) data;
            if (map.size() == 0) {
                return true;
            }
            return false;
        } else if (data.getClass().isArray()) { // Array
            Object[] array = (Object[]) data;
            if (array.length == 0) {
                return true;
            }
            return false;
        } else {
            return false;   // 不知道data 类型 不为null 即 是非空
        }
    }

    public static boolean haveEmpty(Object... objects) {
        if (isEmpty(objects)) {
            return true;
        }
        for (Object object : objects) {
            if (isEmpty(object)) {
                return true;
            }
        }
        return false;
    }


    public static <T extends Iterable> void forEach(Supplier<T> supplier, Consumer consumer) {
        if (!isNull(supplier)) {
            T a = supplier.get();
            a.forEach(consumer);
        }
    }


    public static boolean isTrue(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool;
    }
}
