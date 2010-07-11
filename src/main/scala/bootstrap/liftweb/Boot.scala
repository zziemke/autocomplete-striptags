package bootstrap.liftweb

import net.liftweb.util.{Helpers, Log, NamedPF}
import net.liftweb.common.{Box, Empty, Full, Failure}
import net.liftweb.http._
import net.liftweb.mapper._
import Helpers._
import com.watchedit.classes._

class Boot {
  def boot {
     LiftRules.addToPackages("com.watchedit")
     LiftRules.resourceNames = "general" :: Nil
  
     AutoComplete.init()
	}
}