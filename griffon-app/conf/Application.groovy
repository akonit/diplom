application {
    title = 'Diplom'
    startupGroups = ['diplom']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "diplom"
    'diplom' {
        model      = 'diplom.DiplomModel'
        view       = 'diplom.DiplomView'
        controller = 'diplom.DiplomController'
    }

}
