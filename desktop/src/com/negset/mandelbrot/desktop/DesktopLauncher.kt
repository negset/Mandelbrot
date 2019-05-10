package com.negset.mandelbrot.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.negset.mandelbrot.Mandelbrot

object DesktopLauncher
{
    @JvmStatic
    fun main(arg: Array<String>)
    {
        val config = LwjglApplicationConfiguration().apply {
            width = 600
            height = 600
            resizable = false
        }
        LwjglApplication(Mandelbrot(), config)
    }
}
