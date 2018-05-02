/*
 * Copyright (c) 2018 Bert Volders
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package bert.inc.docs

import java.io.FileReader

import javax.script.{Compilable, Invocable, ScriptEngine, ScriptEngineManager}

import scala.collection.immutable.Range
import java.util.concurrent.TimeUnit
import java.util.concurrent.Executors
import javax.script.ScriptException
import java.util.concurrent.Callable

object Main extends App {
  val executor = Executors.newCachedThreadPool
  val ENGINE_NAME = "nashorn"
  val engine = new ScriptEngineManager()
    .getEngineByName(ENGINE_NAME)
    .asInstanceOf[ScriptEngine with Invocable with Compilable]

  engine.eval("self=this;global=this;")
  /*engine.eval(
    """
      try {
        load('classpath:monaco.js');
      } catch (e) {
        print(e);
      }
    """.stripMargin)*/




  engine.eval("load('classpath:monaco.js')")
  Thread.sleep(1000)
  engine.eval("load('classpath:template.js')")



  val addition = new Callable[Object]() {
    override def call: Object = try {
      engine.eval("onLoad();")
      engine.eval("print('test');")
      engine.eval("print('is init function' + schema.init instanceof Function);")
    } catch {
      case e: ScriptException =>
        throw new RuntimeException(e)
    }
  }

  Range(1,10).foreach{ _ =>
    Thread.sleep(500)
    executor.submit(addition)
    Thread.sleep(100)
  }

  executor.awaitTermination(20, TimeUnit.SECONDS)
  executor.shutdownNow
}
