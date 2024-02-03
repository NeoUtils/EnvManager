package  com.neo.envmanager.model

import com.neo.envmanager.util.extension.json
import java.io.File

@JvmInline
value class FilePromise(val file: File) {

    constructor(dir: File, tag: String) : this(dir.resolve(tag.json))
    constructor(path: String) : this(File(path))
}