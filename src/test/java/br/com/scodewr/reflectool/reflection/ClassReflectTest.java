package br.com.scodewr.reflectool.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassReflectTest {

    @Test
    void classIsLoadedSuccessfullyWhenPackageNameAndClassNameIsValid(){
        var className = "GetterReflect";
        var packageName = "br/com/scodewr/reflectool/reflection/";
        var aClass = ClassReflect.getClassByNameAndPackage(className, packageName);

        Assertions.assertNotNull(aClass);
        Assertions.assertEquals("GetterReflect", aClass.getSimpleName());
    }
}
