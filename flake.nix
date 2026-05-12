{
  description = "netbeans, java, maven for IPC";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
  };

  outputs = { self, nixpkgs }:
    let
      system = "x86_64-linux";
      pkgs = import nixpkgs { inherit system; };

      libs = [
        pkgs.glib
        pkgs.xorg.libX11
        pkgs.gtk3
        pkgs.cairo
        pkgs.pango
        pkgs.gdk-pixbuf
        pkgs.xorg.libXtst
        pkgs.xorg.libXxf86vm
        pkgs.libGL
        pkgs.xorg.libXi 
      ];
    in {
      devShells.${system}.default = pkgs.mkShell {
        packages =
          [
            pkgs.netbeans
            # javaPackages.openjfx21
            # (pkgs.jdk21.override {enableJavaFX = true;})
            pkgs.scenebuilder
            pkgs.jdt-language-server
            pkgs.jdk21
            pkgs.maven
          ];
        LD_LIBRARY_PATH = pkgs.lib.makeLibraryPath libs;
      };
    };
}
