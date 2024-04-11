package furhatos.app.furhatwhoami.flow.main

import furhatos.app.furhatwhoami.flow.Parent
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.File
import java.io.IOException

val Camera: State = state(Parent) {
    onEntry {
        furhat.say("I take a picture!")
        val ffmpegListCommand = "ffmpeg -f avfoundation -list_devices true -i \"\""
        executeCommand(ffmpegListCommand)
        //val ffmpegCommand = "ffmpeg -f avfoundation -framerate 30 -video_size 640x480 -i \"0\" -frames:v 1 intellij.jpg"
        val shellScript = "/Users/thomas/Projects/FurhatWhoAmI/src/main/kotlin/furhatos/app/furhatwhoami/flow/main/capture_image.sh"
        executeShellScript(shellScript)
    }

    onResponse<Yes> {
        furhat.say("you said yes")
    }

    onResponse<No> {
        furhat.say("Ok.")
    }

}
fun executeCommand(command: String) {
    try {
        val process = Runtime.getRuntime().exec(command)

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }
        while (errorReader.readLine().also { line = it } != null) {
            println(line)
        }

        process.waitFor()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun executeShellScript(scriptPath: String) {
    try {
        val processBuilder = ProcessBuilder(scriptPath)
        processBuilder.directory(File(scriptPath).parentFile) // Set the working directory
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

        val process = processBuilder.start() // Start the process
        process.waitFor() // Wait for the process to complete
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
}
