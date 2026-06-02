# To learn more about how to use Nix to configure your environment
# see: https://developers.google.com/idx/guides/customize-idx-env
{ pkgs, ... }: {
  # Which channel of the Nixpkgs repository to use.
  channel = "stable-23.11"; # Use the stable Nixpkgs channel

  # Use https://search.nixos.org/packages to find packages
  packages = [
    pkgs.jdk17
    pkgs.unzip
    pkgs.zip
    pkgs.git
  ];

  # Sets environment variables in the workspace
  env = {
    JAVA_HOME = "${pkgs.jdk17}/lib/openjdk";
  };

  idx = {
    # Search for the extensions you want on https://open-vsx.org/
    extensions = [
      "vscjava.vscode-java-pack"
      "kotlin"
      "mathiasfroehlich.Kotlin"
    ];

    # Enable previews and customize configuration
    previews = {
      enable = true;
      previews = {
        # If running Android preview inside IDX is desired:
        # android = {
        #   command = ["./gradlew" "assembleDebug"];
        #   manager = "android";
        # };
      };
    };

    workspace = {
      # Runs when a workspace is first created
      onCreate = {
        # You can add setup scripts here if needed
      };
      # Runs when a workspace is restarted
      onStart = {
        # Commands to run on start
      };
    };
  };
}
