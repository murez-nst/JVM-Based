package com.murez

import com.murez.util.Loop

Loop.go {
    println 'Just print once'
} along { false }

println '___________'

Loop.go {
    println _key()
} until 5

println '___________'

int i = 0
Loop.go {
    println "i = $i | index = ${ _key() }"
    ++i
} along { i < 5 }

def list = [ 'Murez', 'Nasution', 'Apache Groovy' ]

Loop.go {
    println "index = ${ _key() } | value = ${ _val() }"
} along list

println '_____________________________________'
println()

def map = [ name: 'Murez Nasution', email: 'murez.nasution@gmail', contact: 963852741 ]

Loop.go {
    println "key = ${ _key() } | value = ${ _val() }"
} along map

def person = [ name: 'Murez Nasution', email: 'murez.nasution@gmail', contact: 963852741 ]

def json = Loop.go person.size() > 0? new StringBuilder('{') : Loop.STOP, {
    next { it.append ',' }
            {
                it.append '"' append _key() append '"' append ':'
                def value
                if((value = _val()) == null || value instanceof Number)
                    it.append value
                else
                    it.append '"' append value append '"'
            }
} along person append '}'

print json

Loop.go {
    println "index = ${ _key() }| $test"
} until 3