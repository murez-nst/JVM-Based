package com.murez

class A {
    private final NON_STATIC_RESOURCE = 'will never be touched'

    static void sayHello(def yourName) {    // (1)
        //new Inner()                       // (2)
        new Callable() {                    // (3)
            @Override
            void to(String name) { }
        }
        println "Hello ${ yourName }!"      // (4)
    }

    static def greeting(String salaam) {
        new Callable() {
            @Override
            void to(String name) {
                println "$salaam $name!"
            }
        }
    }

    class Inner { }

    interface Callable {
        void to(String name)
    }
}

def sayHello = A.greeting('Hello')
def sayHi = A.greeting('Hi')
sayHello.to 'World'
sayHello.to 'Groovy'
sayHi.to 'Murez'


def result = A.sayHello('World')
println result                          // (5)


class B {
    private String salaam

    void say(String name) {
        println "$salaam $name!"        // (1)
    }
}

def withHello = new B(salaam: 'Hello')
def withHi = new B(salaam: 'Hi')
withHello.say 'World'                   // (2)
withHi.say 'Murez'



class Any {
    private myName

    String getName() { myName }         // (1)
}

def say = {
    println "$it ${ name }!"            // (2)
}

say.delegate = new Any(myName: 'World') // (3)
say 'Hello'                             // (4)
say.delegate = new Any(myName: 'Murez')
say 'Hi'


class C {
    static def isPositive = { int it -> it > 0 }

    static def isPositive(def number) {
        throw new UnsupportedOperationException()
    }

    static def test(int value) {
        isPositive.call(value)           // (1)
    }
}

println C.test(1)



class Outer {
    class Inner {
        def takeOwner() {
            ({ getOwner() })                                // (1)
        }

        Closure 'Take a Nested closure'() {
            ({                                              // (2)
                ({ owner })()                               // (3)
            })
        }
    }

    void test() {
        def inner = new Inner()                             // (4)
        assert inner.takeOwner()() == inner                 // (5)
        Closure nested = inner.'Take a Nested closure'()    // (6)
        assert nested() == nested                           // (7)
        assert ({ owner })() == this                        // (8)
        println 'Successful!'                               // (9)
    }
}

new Outer().test()


/*
def logger = { String prefix, Closure write ->
    ({ String message ->
        String now = format(new Date())
        write "[$prefix]${ now? "[$now]" : '' } — $message"
    })
}

logger.delegate = new SimpleDateFormat('yyyy-MM-dd:HH-mm-ss:S')

def debugLogger = logger 'DEBUG', System.out.&println
def errorLogger = logger 'ERROR', System.err.&println

debugLogger 'Hi! I am a debugger'
errorLogger 'No!'
*/