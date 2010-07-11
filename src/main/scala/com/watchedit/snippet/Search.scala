package com.watchedit {
package snippet {

import net.liftweb.util.Helpers._
import net.liftweb.http._
import S._
import scala.xml._
import classes._

class Search {
	object query extends SessionVar("")
	def box(xhtml: NodeSeq) : NodeSeq =
		bind("search", xhtml,
		    "query" -> AutoComplete(
		      query.is, 
		      buildQuery _,
		      query(_),
		      List(("selectFirst", "false"),
		           ("minChars", "0"),
		           ("formatItem", """function(row, i, max) {
               			return "<img src='" + row[1] + "' /> <b>" + row[0] + "</b>";
               		}""")),
		      ("style", "width: 280px")))
		
	private def buildQuery(current: String, limit: Int): Seq[(String, String, String)] = Search.results
}

object Search {
  val results = ("Item 1", "item.png", "/items/1") :: ("Item 2", "item.png", "/items/2") :: ("Item 3", "item.png",  "/items/3") :: Nil
}
}}