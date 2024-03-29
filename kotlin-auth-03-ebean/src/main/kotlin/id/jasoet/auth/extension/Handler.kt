package id.jasoet.auth.extension

import io.ktor.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

typealias Handler = suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit