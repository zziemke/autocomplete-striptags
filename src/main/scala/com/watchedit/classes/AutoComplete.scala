/*
 * Copyright 2007-2010 WorldWide Conferencing, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.watchedit {
package classes {

import scala.xml.{NodeSeq, Node, Elem, PCData, Text, Unparsed}
import net.liftweb.common._
import net.liftweb.util._
import net.liftweb.http._
import net.liftweb.http.js._
import JsCmds._
import JE._
import S._
import SHtml._
import Helpers._

object AutoComplete { 
  def apply(start: String, 
            options: (String, Int) => Seq[(String, String, String)],
            onSubmit: String => Unit, 
            jsonOptions: List[(String,String)],
            attrs: (String, String)*) = new AutoComplete().render(start, options, onSubmit, jsonOptions ,attrs:_*)  
    
  /**
   * register the resources with lift (typically in boot)
   */
  def init() {
    import net.liftweb.http.ResourceServer

    ResourceServer.allow({
        case "autocomplete" :: _ => true
     })
  }

}

class AutoComplete {
  /**
   * Render a text field with Ajax autocomplete support
   * 
   * @param start - the initial input string
   * @param option - the function to be called when user is typing text. The text and th options limit is provided to this functions
   * @param attrs - the attributes that can be added to the input text field 
   * @param jsonOptions - a list of pairs that will be send along to the jQuery().AutoComplete call (for customization purposes)
   */
   def render(start: String, 
              options: (String, Int) => Seq[(String, String, String)], 
              onSubmit: String => Unit, 
              jsonOptions: List[(String, String)], 
              attrs: (String, String)*): Elem = {
    
    val f = (ignore: String) => {
      val q = S.param("q").openOr("")
      val limit = S.param("limit").flatMap(asInt).openOr(10)
      PlainTextResponse(options(q, limit).map(item => item._1 + "|" + item._2 + "|" + item._3).mkString("\n"))
    }


    fmapFunc(SFuncHolder(f)){ func =>
      val what: String = encodeURL(S.contextPath + "/" + LiftRules.ajaxPath+"?"+func+"=foo")

      fmapFunc(SFuncHolder(onSubmit)){id =>

     /* merge the options that the user wants */
      val jqOptions =  ("minChars","0") ::
                       ("matchContains","true") ::
                       Nil ::: jsonOptions
      val json = jqOptions.map(t => t._1 + ":" + t._2).mkString("{", ",", "}")
      val autocompleteOptions = JsRaw(json)
    
      val onLoad = JsRaw("""
      jQuery(document).ready(function(){
        var data = """+what.encJs+""";
        jQuery("#"""+id+"""").autocomplete(data, """+autocompleteOptions.toJsCmd+""").result(function(event, dt, formatted) {
        location.href = dt[2];
        });
      });""")

      <span>
        <head>
          <link rel="stylesheet" href={"/" + LiftRules.resourceServerPath +"/autocomplete/jquery.autocomplete.css"} type="text/css" />
          <script type="text/javascript" src={"/" + LiftRules.resourceServerPath +"/autocomplete/jquery.autocomplete.js"} />
          <script type="text/javascript">{Unparsed(onLoad.toJsCmd)}</script>
        </head>
        {
          attrs.foldLeft(<input type="text" name={id} id={id} value={start} />)(_ % _)
        }
      </span>
    }
   }
  }
}

}}