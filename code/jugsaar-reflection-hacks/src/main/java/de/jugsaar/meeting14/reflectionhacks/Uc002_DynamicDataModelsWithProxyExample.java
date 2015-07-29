package de.jugsaar.meeting14.reflectionhacks;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 29.07.15.
 */
public class Uc002_DynamicDataModelsWithProxyExample {


    interface DataModel {

        String getStringValue();

        void setStringValue(String value);

        int getIntValue();

        void setIntValue(int value);
    }

    public static void main(String[] args) {

        DataModel dataModel = ModelProxyFactory.createModel(DataModel.class);
        dataModel.setStringValue("ABC");
        dataModel.setIntValue(1337);

        System.out.println(dataModel.getStringValue());
        System.out.println(dataModel.getIntValue());

        System.out.println(dataModel);
    }


    static class DataModelWrapper extends AbstractInvocationHandler { //AbstractInvocationHandler from google guava

        private final Class<?> modelType;

        private final Map<String, Object> propertyValues = new HashMap<String, Object>();

        public DataModelWrapper(Class<?> modelType) {
            this.modelType = modelType;
        }

        @Override
        protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {

            String methodName = method.getName();

            if (methodName.startsWith("get")) {
                String attributeName = getAttributeName(method.getName());
                return propertyValues.get(attributeName);
            }

            if (methodName.startsWith("set")) {
                String attributeName = getAttributeName(method.getName());
                propertyValues.put(attributeName, args[0]);
                return null;
            }

            return null;
        }

        @Override
        public String toString() {
            return modelType.getSimpleName() + " " + propertyValues.toString();
        }

        private String getAttributeName(String name) {
            return Character.toLowerCase(name.charAt(3)) + name.substring(4);
        }
    }

    static class ModelProxyFactory {

        public static <M> M createModel(Class<M> modelType) {
            return Reflection.newProxy(modelType, new DataModelWrapper(modelType));
        }

        public static <M> M createModel(M... args) {
            return createModel((Class<M>) args.getClass().getComponentType());
        }
    }
}
