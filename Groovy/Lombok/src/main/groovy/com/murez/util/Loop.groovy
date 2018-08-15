package com.murez.util

import static groovy.lang.Closure.DELEGATE_FIRST as X
/**
 * @author Murez Nasution
 */
class Loop {
    static final STOP = new Object()

    static go(def init = null, Closure loop) { new Loop(init, loop) }

    private Closure loop, next
    private v, k = 0
    private init
    private test = 'No! I am private.'

    private Loop(def init, Closure loop) {
        this.init = init
        this.loop = loop
    }

    def until(int last) {
        if(init == STOP) return STOP
        def o
        try { o = 'do'() }
        catch(NullPointerException | NoSuchLoopException ignored) { return }
        catch(e) { throw e }
        if(k < last) {
            if(next) {
                for(; ++k < last; ) o = loop next(o)
            } else
                for(; ++k < last; ) o = loop o
        }
    }

    def along(Closure<Boolean> condition) {
        if(init == STOP) return STOP
        def o
        try { o = 'do'() }
        catch(NullPointerException | NoSuchLoopException ignored) { return }
        catch(e) { throw e }
        if(condition) {
            if(!next) {
                for(; condition(++k); ) o = loop o
            } else
                for(; condition(++k); ) o = loop next(o)
        }
    }

    def along(Iterable collection) {
        if(init == STOP) return STOP
        def i, o
        try {
            i = collection.iterator()
            if(!(o = i.hasNext())) return o
            v = i.next()
            o = 'do'()
        }
        catch(NullPointerException | NoSuchLoopException ignored) { return }
        catch(e) { throw e }
        if(next) {
            for(; i.hasNext(); o = loop o) {
                o = next o
                ++k
                v = i.next()
            }
        } else
            for(; i.hasNext(); o = loop o) {
                ++k
                v = i.next()
            }
        o
    }

    def along(Map entryPairs, Closure<Boolean> ctrl = null) {
        if(init == STOP) return STOP
        def o, i
        try {
            i = entryPairs.entrySet().iterator()
            if(!(o = i.hasNext())) return o
            set i
            o = 'do'() }
        catch(NullPointerException | NoSuchLoopException ignored) { return }
        catch(e) { throw e }
        if(next) {
            for(; i.hasNext(); o = loop o) {
                o = next o
                set i
            }
        } else
            for(; i.hasNext(); o = loop o) set i
        o
    }

    private void set(Iterator<Map.Entry> i) {
        def e = i.next()
        k = e.key
        v = e.value
    }

    private 'do'() {
        try { loop.resolveStrategy = X }
        catch(e) { throw e }
        def args = [ flag: true ]
        loop.delegate = new Delegator({ k }, { v }, {
            nextAction, mainAction ->
                next = nextAction
                args.main = mainAction
                args.flag = null
        })
        def o = loop init
        if(!args.flag) {
            if(!args.main) throw new NoSuchLoopException()
            loop = args.main as Closure
            loop init
        } else o
    }

    private final class Delegator {
        Closure<Void> next
        Closure _key, _val

        private Delegator(key, val, setter) {
            _key = key
            _val = val
            next = setter
        }
    }

    private final class NoSuchLoopException extends RuntimeException {
        private NoSuchLoopException() {
            super('No action to be performed')
        }
    }
}