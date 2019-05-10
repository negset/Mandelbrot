package com.negset.mandelbrot

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

const val PIXEL = 600

class Mandelbrot : ApplicationAdapter()
{
    private val batch by lazy { SpriteBatch() }
    private lateinit var texture: Texture

    private var size = 4.0
    private val centers = mutableListOf(Complex(0.0, 0.0))

    override fun create()
    {
        Gdx.input.inputProcessor = object : InputAdapter()
        {
            override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean
            {
                when (button)
                {
                    Input.Buttons.LEFT -> zoomIn()
                    Input.Buttons.RIGHT -> zoomOut()
                }
                return true
            }
        }
        updateTexture()
    }

    override fun render()
    {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(texture, 0f, 0f)
        batch.end()
    }

    private fun coordinateToComplex(x: Int, y: Int): Complex
    {
        val r = centers.last().r + (x - PIXEL / 2) * size / PIXEL
        val i = centers.last().i + (y - PIXEL / 2) * size / PIXEL
        return Complex(r, i)
    }

    private fun zoomIn()
    {
        centers.add(coordinateToComplex(Gdx.input.x, Gdx.input.y))
        size /= 2
        updateTexture()
    }

    private fun zoomOut()
    {
        if (centers.size == 1) return
        centers.removeAt(centers.lastIndex)
        size *= 2
        updateTexture()
    }

    private fun mandelbrot(c: Complex): Int
    {
        var x = 0.0
        var y = 0.0
        for (i in 0 until 50)
        {
            val x2 = x * x - y * y + c.r
            val y2 = 2.0 * x * y + c.i
            x = x2
            y = y2
            if (x * x + y * y > 4) return i
        }
        return -1
    }

    private fun updateTexture()
    {
        val pixmap = Pixmap(PIXEL, PIXEL, Pixmap.Format.RGBA8888)

        val center = centers.last()
        for (x in 0 until PIXEL)
        {
            for (y in 0 until PIXEL)
            {
                val r = (center.r - size / 2) + size * x / PIXEL
                val i = (center.i - size / 2) + size * y / PIXEL
                when (val m = mandelbrot(Complex(r, i)))
                {
                    -1 -> pixmap.setColor(Color.WHITE)
                    else ->
                    {
                        pixmap.setColor(Color(1f, 0f, 0f, m / 10f))
                    }
                }
                pixmap.drawPixel(x, y)
            }
        }

        texture = Texture(pixmap)
        pixmap.dispose()
    }

    override fun dispose()
    {
        batch.dispose()
    }
}

data class Complex(val r: Double, val i: Double)
