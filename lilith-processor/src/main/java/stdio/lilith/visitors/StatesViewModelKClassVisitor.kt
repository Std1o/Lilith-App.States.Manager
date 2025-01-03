package stdio.lilith.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import stdio.lilith.UsedPackages.loadableDataPackage
import stdio.lilith.UsedPackages.operationStatePackage
import java.io.OutputStream

internal class StatesViewModelKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val dependencies: Dependencies,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = "lilith.presentation.viewmodel"

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "StatesViewModel"
        )

        val content = this.javaClass.classLoader.getResource("StatesViewModel.txt")?.readText()

        outputStream.write(
            ("""
    |@file:Suppress("UNCHECKED_CAST")
    |@file:OptIn(ExperimentalReflectionOnLambdas::class)

    |package $packageName

    |import $operationStatePackage.OperationState
    |import $loadableDataPackage.LoadableData
    """.trimMargin() + "\n" + content).trimMargin().toByteArray()
        )

    }
}