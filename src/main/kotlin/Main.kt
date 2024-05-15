package it.posteitaliane.dckli

import java.io.ByteArrayInputStream

fun main() {

    val example = """
        ; last modified 1 April 2001 by John Doe
        [owner]
        name = John Doe
        organization = Acme Widgets Inc.

        [database]
        ; use IP address in case network name resolution is not working
        server = 192.0.2.62     
        port = 143
        file = "payroll.dat"
    """.trimIndent()

    val nosections = """
          key1=value1
        \tkey2 = value2\s\s
    """.trimIndent()

    val parser = InitParser(ByteArrayInputStream(nosections.toByteArray()))
    parser.parse()

}