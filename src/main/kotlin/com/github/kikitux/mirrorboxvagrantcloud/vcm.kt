package com.github.kikitux.mirrorboxvagrantcloud

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hashicorp.checkpoint.CheckPointData
import com.hashicorp.checkpoint.getCheckPointData
import com.hashicorp.releases.getRelease
import com.vagrantup.app.*
import kotlin.system.exitProcess
import joptsimple.OptionSet
import java.io.File

fun vcm(options: OptionSet) {

    // if -h or --help, exit 0 + help
    if ( options.has("h") || options.has( "help" ) ) {
        halp(0)
    }

    // if -v or --vagrant
    if ( options.has( "v") || options.has( "vagrant") ) {
        val checkpoint = getCheckPointData("vagrant")                           // get checkpoint data
        val release = getRelease(checkpoint.product, checkpoint.current_version)        // get release info
        val outDirectory = "mirror/${checkpoint.product}/${checkpoint.current_version}" // set out directory

        // download the selected product release
        downloadRelease(release, "x86_64", outDirectory)
    }

    // process user value
    if ( options.has( "u") ) {
        if ( options.valuesOf("u").count() > 1 ) {
            println("info: got more than 1 user/org, will use the first one only")
            println(options.valuesOf( "u" )[0])
        }

        // todo provider here

        val user = options.valuesOf( "u" )[0]
        val apiuserdata = getApiUserData(user.toString())

        val privateBoxes = apiuserdata.boxes.filter { it.private } // true
        val publicBoxes = apiuserdata.boxes.filter { !it.private } // false


        // list boxes or process the ones we got
        if ( options.valuesOf("b").count() < 1) {
            // no boxes so we show what we got

            println("listing boxes with released status")

            print("\n\n")
            println("private boxes:")
            println("(will be shown if VAGRANT_CLOUD_TOKEN is set and we have access)")
            privateBoxes.onEach {
                if (it.current_version != null) {
                    println(it.name + "\t" + it.current_version.number)
                    it.current_version.providers.onEach {
                        print(it.name + " ")
                    }
                    print("\n\n")
                }
            }

            print("\n\n")
            println("public boxes:")
            publicBoxes.onEach {
                if (it.current_version != null) {
                    println(it.name + "\t" + it.current_version.number)
                    it.current_version.providers.onEach {
                        print(it.name + " ")
                    }
                    print("\n\n")
                }
            }


        } else {
            // boxes list so we use this to download
            val listBoxes = apiuserdata.boxes.filter { it.name in options.valuesOf("b") }

            listBoxes.onEach {

                var currentbox = newbox() // so we can create a json to be consumed
                val box = it.name

                if (it.current_version != null) {
                    currentbox.description = it.description_html
                    currentbox.name = it.name
                    currentbox.short_description = it.short_description
                    currentbox.versions[0].version = it.current_version.number
                    currentbox.versions[0].status = it.current_version.status
                    currentbox.versions[0].description_html = it.current_version.description_html
                    currentbox.versions[0].description_markdown = it.current_version.description_html

                    val version = it.current_version.version
                    var providers: List<APIUserBoxProvider>

                    if (options.valuesOf("p").count() < 1) {
                        println("no provider defined")

                        providers = it.current_version.providers

                    } else {
                        println("yay pryvider defined")
                        providers = it.current_version.providers.filter { it.name in options.valuesOf("p") }
                    }

                    var i = 0
                    providers.onEach {
                        val outDir = "mirror/${user}/${box}/${version}"
                        val file = it.name + ".box"
                        getFiles(outDir, file , it.download_url)
                        val newprovider = BOXProvider(it.name,outDir + "/" + file) // todo cwd
                        currentbox.versions[0].providers.add(i,newprovider)
                        i++

                    }
                }

                // create a json for the box and providers
                val mapper = jacksonObjectMapper()
                println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currentbox))

                mapper.writerWithDefaultPrettyPrinter().writeValue( File("mirror/${user}/${box}.json"), currentbox);

            }

        }

    }



}

fun halp(exitCode: Int){
    println("vcm <command> [option1 .. optionN]")

    val halp = """
        vcm <-h|--help>     // display this window
        vcm <-v|--vagrant>  // download current vagrant option
        vcm <-u> <user|org> // set user or org

        No box argument will display list of boxes for given user

        vcm <-u> <user|org> <-b|--box> <boxname> .. <-b|--box> <boxname>

        If box argument given, will download the box

        If provider given, <-p|--provider> <providernane> will download only that provider

        ie.

        vcm -u hashicorp -b precise64 -p virtualbox

        will download hashicorp/precise64 for virtualbox on:

        mirror/hashicorp/precise64/<version>/virtualbox.box


        """
            .trimIndent()

    println(halp)

    exitProcess(exitCode)
}