import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtilities {
    public static Method getPublicStaticMethod(String name, Class<?> clientClass, Class<?> retClass, Class<?>... params) {
        Method method = getMethod(name, clientClass, retClass, params);
        if (!Modifier.isPublic(method.getModifiers())) {
            fail(String.format("%s method is not public", name));
        }

        if (!Modifier.isStatic(method.getModifiers())) {
            fail(String.format("%s method is not static", name));
        }

        return method;
    }
    
    public static Method getPublicInstanceMethod(String name, Class<?> clientClass, Class<?> retClass, Class<?>... params) {
        Method method = getMethod(name, clientClass, retClass, params);
        if (!Modifier.isPublic(method.getModifiers())) {
            fail(String.format("%s method is not public", name));
        }

        if (Modifier.isStatic(method.getModifiers())) {
            fail(String.format("%s method is static (should be an instance method, non-static)", name));
        }

        return method;
    }
    
    public static Method getMethod(String name, Class<?> clientClass, Class<?> retClass, Class<?>... params) {
        try {
            Method method = clientClass.getDeclaredMethod(name, params);
            if (!method.getReturnType().equals(retClass)) {
                fail(String.format("%s method does not return a %s object", name, retClass.getName()));
            }
            return method;
        } catch (NoSuchMethodException | SecurityException e) {
            fail(String.format("No %s method found in %s class or using incorrect parameters", name,
                    clientClass.getName()));
        }
        return null;
    }
    
    public static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException | SecurityException e) {
            fail(String.format("No %s field found in %s class", name, clazz.getName()));
        }
        return null;
    }

    public static <T> Object getFieldReference(T obj, String name) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            fail(String.format("No %s field found in %s class", name, obj.getClass().getName()));
        }
        return null;
    }

    public static <T> List<?> getFieldReferences(T obj) {
        try {
            var fields = obj.getClass().getDeclaredFields();
            var list = new ArrayList<>(fields.length);
            for (Field field : fields) {
                field.setAccessible(true);
                list.add(field.get(obj));
            }
            return list;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            fail("Cannot access fields");
        }
        return null;
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException | SecurityException e) {
            fail(String.format("No constructor found in %s class or using incorrect parameters", clazz.getName()));
        }
        return null;
    }
    
    public static <T> void assertFieldValueEquals(T expected, Object obj, Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            Object result = field.get(obj);
            assertEquals(expected, result);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            fail(String.format("No field %s found in %s class", name, clazz.getName()));
        }
    }
    
    public static Object invoke(Method method, Object obj, Object... args) {
        try {
            return method.invoke(obj, args);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            fail(String.format("Unable to invoke method %s", method.getName()));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                RuntimeException re = (RuntimeException) e.getCause();
                throw re;
            } else {
                fail(String.format("%s method threw a checked exception", method.getName()));
            }
        }
        return null;
    }
    
    public static <T> T newInstance(Constructor<T> constr, Object... args) {
        try {
            return constr.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            fail(String.format("Unable to invoke constructor %s", constr.getName()));
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
                RuntimeException re = (RuntimeException) e.getCause();
                throw re;
            } else {
                fail(String.format("Constructor %s threw a checked exception", constr.getName()));
            }
        }
        return null;
    }
    
    public static <T> void checkFieldsEncapsulation(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            assertTrue("Field " + field.getName() + " is not private", Modifier.isPrivate(field.getModifiers()));
            assertFalse("Field " + field.getName() + " is static", Modifier.isStatic(field.getModifiers()));
        }
    }

    public static <T> void demandExactFieldTypes(Class<T> clazz, Class<?>... expectedFields) {
        Field[] fields = clazz.getDeclaredFields();
        LinkedList<Class<?>> expectedList = new LinkedList<>(Arrays.asList(expectedFields));
        for (Field field : fields) {
            Class<?> actualType = field.getType();
            assertTrue(String.format("Field of type %s not allowed", actualType),expectedList.remove(actualType));
        }
        assertTrue("Not enough fields", expectedList.isEmpty());
    }
    
    public static <T> void checkRedundancy(Class<T> clazz, Method... allowedPublicMethods) {
        LinkedList<Method> methods = new LinkedList<>(Arrays.asList(clazz.getDeclaredMethods()));
        
        for (Method method : allowedPublicMethods) {
            methods.remove(method);
        }
        assertTrue("no 'helper' methods found; code very likely has redundancy", !methods.isEmpty());
        
        for (Method method : methods) {
            assertTrue("Helper method " + method.getName() + " is not private", Modifier.isPrivate(method.getModifiers()));
        }
    }
    
    public static String getOutput(Consumer<PrintStream> consumer) {
        final Charset charset = StandardCharsets.UTF_8;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PrintStream ps = new PrintStream(baos, true, charset.name());
            consumer.accept(ps);
            String content = new String(baos.toByteArray(), charset);
            ps.close();
            baos.close();
            
            return content;
        } catch (IOException e) {
            fail("IOException while performing operation");
        }
        return null;
    }
    
    public static String getConsoleOutput(Runnable method) {
        final Charset charset = StandardCharsets.UTF_8;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
//            PrintStream ps = new PrintStream(baos, true, charset.name());
//            consumer.accept(ps);
            PrintStream old = System.out;
            System.setOut(new PrintStream(baos, true, charset.name()));
            method.run();
            String content = new String(baos.toByteArray(), charset);
            System.setOut(old);
            baos.close();
            return content;
        } catch (IOException e) {
            fail("IOException while performing operation");
        }
        return null;
    }
    
    public static void checkLineByLine(String expected, String actual) {
        Scanner eScanner = new Scanner(expected);
        Scanner aScanner = new Scanner(actual);
        
        while (aScanner.hasNextLine()) {
            String aLine = aScanner.nextLine();
            assertTrue("did not expect any output", eScanner.hasNextLine());
            String eLine = eScanner.nextLine();
            
            assertEquals(eLine.trim(), aLine.trim());
        }
        
        assertFalse("expecting more output", eScanner.hasNextLine());
        
        eScanner.close();
        aScanner.close();
    }
    
    public static IntGraph createCompleteGraph(IntGraph graph) {
        for (int i = 0; i < graph.getNumVertices(); i++) {
            for (int j = i + 1; j < graph.getNumVertices(); j++) {
                graph.addEdge(i, j);
            }
        }
        return graph;
    }
    
    public static IntGraph createGraph(IntGraph graph, int... edgeList) {
        assertTrue(edgeList.length % 2 == 0);
        for (int i = 0; i < edgeList.length; i += 2) {
            graph.addEdge(edgeList[i], edgeList[i + 1]);
        }
        
        return graph;
    }

    @SafeVarargs
    public static <T> Map<Integer, T> createMap(T... values) {
        HashMap<Integer,T> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            map.put(i, values[i]);
        }
        return map;
//        return IntStream.range(0, values.length)
//                .boxed()
//                .collect(Collectors.toMap(i -> i, i -> values[i]));
    }

    public static <T> Map<Integer,T> createMapSingleValue(int n, T value) {
        return IntStream.range(0, n)
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> value));
    }

    public static IntGraph createSlidesGraph(IntGraph graph) {
        return createGraph(graph,
                0, 1, //
                0, 2, //
                1, 2, //
                1, 3, //
                1, 4, //
                2, 3, //
                3, 4, //
                4, 5, //
                4, 6, //
                4, 7, //
                4, 11, //
                5, 6, //
                5, 8, //
                6, 8, //
                6, 10, //
                7, 10, //
                7, 11, //
                8, 9, //
                9, 11);
    }

    public static IntGraph createDisconnectedGraph(IntGraph graph) {
        return createGraph(graph,
                0, 1, //
                0, 2, //
                1, 2, //
                1, 3, //
//                1, 4, //
                2, 3, //
//                3, 4, //
                4, 5, //
                4, 6, //
                4, 7, //
                4, 11, //
                5, 6, //
                5, 8, //
                6, 8, //
                6, 10, //
                7, 10, //
                7, 11, //
                8, 9, //
                9, 11);
    }

}
