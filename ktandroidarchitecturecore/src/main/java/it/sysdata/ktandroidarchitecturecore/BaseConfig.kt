package it.sysdata.ktandroidarchitecturecore

import it.sysdata.ktandroidarchitecturecore.platform.SafeExecute
import it.sysdata.ktandroidarchitecturecore.platform.SafeExecuteInterface

object BaseConfig {


    var safeExecutor: SafeExecuteInterface = SafeExecute()


}