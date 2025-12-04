import { resolve } from 'path';
import { defineConfig, mergeConfig, type UserConfigFn } from 'vite';
import { vaadinConfig } from './vite.generated';

import { loadPrivateFilesFromLocalFolder, writeEnvFile } from '../copilot-internal/copilot/vite-util.js';

const useBundle = process.env.USE_BUNDLE === 'true';

writeEnvFile();
const customConfig: UserConfigFn = (_env) => ({
    envDir: resolve('.'),
    server: { fs: { allow: [resolve('../..')] } },
    resolve: {
        alias: {
            Copilot: resolve('../copilot-internal/copilot/frontend/copilot'),
            CopilotPrivate: resolve('../copilot-internal/copilot-private/frontend/copilot/private'),
            Shared: resolve('../copilot-internal/copilot/frontend/copilot/shared'),
        },
    },
    plugins: [useBundle && loadPrivateFilesFromLocalFolder()],
});

const conf = defineConfig((env) => {
    const merged = mergeConfig(vaadinConfig(env), customConfig(env));

    // This loads the files from the copilot package instead of using the built bundle
    if (!useBundle) {
        merged.resolve.alias = {
            'Frontend/generated/jar-resources/copilot': resolve('../copilot-internal/copilot/frontend/copilot'),
            'Frontend/generated/jar-resources/copilot.js': resolve('../copilot-internal/copilot/frontend/copilot.js'),
            ...merged.resolve.alias,
        };
    }
    return merged;
});
export default conf;